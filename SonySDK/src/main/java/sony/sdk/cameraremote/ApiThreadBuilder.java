/*
 * Copyright 2014 Sony Corporation
 * Modified by stackoverflow user Keysersoze
 */

package sony.sdk.cameraremote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import sony.sdk.cameraremote.utils.SimpleHttpClient;
import sony.sdk.cameraremote.CommandThreadCallback;
/**
 * Created by aaron on 7/19/16.
 */

public class ApiThreadBuilder {

    private String TAG = ApiThreadBuilder.class.getSimpleName();

    private static ServerDevice mTargetServer;
    private HashMap<String, JSONObject> apiMap;
    private static ApiThreadBuilder remoteApiInstance = null;
    private int mRequestId;
    private CommandThreadCallback threadCallback;
    ExecutorService service

    private ApiThreadBuilder() throws IOException{
        try {
            apiMap = ApiMap.getApiMap();
        } catch (JSONException e) {
            throw new IOException(e);
        }

    }

    public static ApiThreadBuilder getInstance() throws IOException {
        if(remoteApiInstance == null) {
            remoteApiInstance = new ApiThreadBuilder();
        }
        return remoteApiInstance;
    }
    public void init(ServerDevice target) {
        mTargetServer = target;
        mRequestId = 1;
        service =  Executors.newSingleThreadExecutor();
    }

    public void setThreadCallback(CommandThreadCallback threadCallback){
        this.threadCallback = threadCallback;
    }

    private JSONObject sendCommandString(final String command, final JSONArray params) throws IOException{

        
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String responseJson = "";
                try {
                    JSONObject jsonCommand = apiMap.get(command);

                    if (params != null) {
                        jsonCommand.put("params", params);
                    }

                    String service = jsonCommand.get("service").toString();
                    jsonCommand.remove("service");
                    jsonCommand.put("id", mRequestId);

                    String url = findActionListUrl(service) + "/" + service;
                    Log.d(TAG, "RequestCommand:  " + command);
                    Log.d(TAG, "Request:  " + jsonCommand.toString());
                    if(command != "getEvent") {
                        responseJson = SimpleHttpClient.httpPost(url, jsonCommand.toString());
                    } else {
                        int longPollingTimeout = (params.getBoolean(0)) ? 20000 : 8000; // msec
                        responseJson = SimpleHttpClient.httpPost(url, jsonCommand.toString(), longPollingTimeout);
                    }

                    Log.d(TAG, "Response: " + responseJson);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {


                }
                mRequestId++;
                return responseJson;
            }
        });

        String response = null;
        JSONObject jsonResponse = null;
        try {
            response = future.get();
            jsonResponse = new JSONObject(response);
            if(threadCallback != null) {
                threadCallback.threadFinished(jsonResponse); 
            }
            threadCallback = null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }


        

        return jsonResponse;
    }

    /**
     * Retrieves Action List URL from Server information.
     *
     * @param service
     * @return
     * @throws IOException
     */
    private static String findActionListUrl(String service) throws IOException {
        List<ServerDevice.ApiService> services = mTargetServer.getApiServices();
        for (ServerDevice.ApiService apiService : services) {
            if (apiService.getName().equals(service)) {
                return apiService.getActionListUrl();
            }
        }
        throw new IOException("actionUrl not found. service : " + service);
    }
    // Camera Service APIs

    /**
     * Calls getAvailableApiList API to the target server. Request JSON data is
     * such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getAvailableApiList",
     *   "params": [""],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getAvailableApiList() throws IOException {
        return sendCommandString("getAvailableApiList", new JSONArray());
    }

    /**
     * Calls getApplicationInfo API to the target server. Request JSON data is
     * such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getApplicationInfo",
     *   "params": [""],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getApplicationInfo() throws IOException {
        return sendCommandString("getApplicationInfo", new JSONArray());
    }

    /**
     * Calls getShootMode API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getShootMode",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getShootMode() throws IOException {
        return sendCommandString("getShootMode", new JSONArray());
    }

    /**
     * Calls setShootMode API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "setShootMode",
     *   "params": ["still"],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param shootMode shoot mode (ex. "still")
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject setShootMode(String shootMode) throws IOException {
        JSONArray param = new JSONArray();
        param.put(shootMode);
        return sendCommandString("setShootMode", param);
    }

    /**
     * Calls getAvailableShootMode API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getAvailableShootMode",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException - all errors and exception are wrapped by this Exception.
     */
    public JSONObject getAvailableShootMode() throws IOException {
        return sendCommandString("getAvailableShootMode", new JSONArray());
    }

    /**
     * Calls getSupportedShootMode API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSupportedShootMode",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getSupportedShootMode() throws IOException {
        return sendCommandString("getSupportedShootMode", new JSONArray());
    }

    /**
     * Calls startContShooting API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startContShooting",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject startContShooting() throws IOException {
        return sendCommandString("startContShooting", new JSONArray());
    }

    /**
     * Calls startContShooting API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startContShooting",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject stopContShooting() throws IOException {
        return sendCommandString("stopContShooting", new JSONArray());
    }

    /**
     * Calls startBlubShooting API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startBlubShooting",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject startBulbShooting() throws IOException {
        return sendCommandString("startBulbShooting", new JSONArray());
    }

    /**
     * Calls stopBlubShooting API to the target server. Request JSON data
     * is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "stopBlubShooting",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject stopBulbShooting() throws IOException {
        return sendCommandString("stopBulbShooting", new JSONArray());
    }

    /**
     * Calls startLiveview API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startLiveview",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject startLiveview() throws IOException {
        return sendCommandString("startLiveview", new JSONArray());
    }

    /**
     * Calls stopLiveview API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "stopLiveview",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject stopLiveview() throws IOException {
        return sendCommandString("stopLiveview", new JSONArray());
    }

    /**
     * Calls startRecMode API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startRecMode",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject startRecMode() throws IOException {
        return sendCommandString("startRecMode", new JSONArray());
    }

    /**
     * Calls stopRecMode API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "stopRecMode",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject stopRecMode() throws IOException {
        return sendCommandString("stopRecMode", new JSONArray());
    }

    /**
     * Calls actHalfPressShutter API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     * "method": "actHalfPressShutter",
     * "params": [],
     * "id": 1,
     * "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException
     */
    public JSONObject actHalfPressShutter() throws IOException {
        return sendCommandString("actHalfPressShutter", new JSONArray());
    }

    /**
     * Calls actTakePicture API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "actTakePicture",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException
     */
    public JSONObject actTakePicture() throws IOException {
        return sendCommandString("actTakePicture", new JSONArray());
    }



    /**
     * Calls startMovieRec API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startMovieRec",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject startMovieRec() throws IOException {
        return sendCommandString("startMovieRec", new JSONArray());
    }

    /**
     * Calls stopMovieRec API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "stopMovieRec",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject stopMovieRec() throws IOException {
        return sendCommandString("stopMovieRec", new JSONArray());
    }

    /**
     * Calls actZoom API to the target server. Request JSON data is such like as
     * below.
     * <p/>
     * <pre>
     * {
     *   "method": "actZoom",
     *   "params": ["in","stop"],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param direction direction of zoom ("in" or "out")
     * @param movement  zoom movement ("start", "stop", or "1shot")
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject actZoom(String direction, String movement) throws IOException {
        JSONArray param = new JSONArray();
        param.put(direction).put(movement);
        return sendCommandString("actZoom", param);
    }

    /**
     * Calls getEvent API to the target server. Request JSON data is such like
     * as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getEvent",
     *   "params": [true],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param longPollingFlag true means long polling request.
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getEvent(boolean longPollingFlag) throws IOException {
        JSONArray param = new JSONArray();
        param.put(longPollingFlag);
        return sendCommandString("getEvent", param);
    }

    /**
     * Calls setCameraFunction API to the target server. Request JSON data is
     * such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "setCameraFunction",
     *   "params": ["Remote Shooting"],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param cameraFunction camera function to set
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject setCameraFunction(String cameraFunction) throws IOException {
        JSONArray param = new JSONArray();
        param.put(cameraFunction);
        return sendCommandString("setCameraFunction", param);
    }

    /**
     * Calls getMethodTypes API of Camera service to the target server. Request
     * JSON data is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getMethodTypes",
     *   "params": ["1.0"],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getCameraMethodTypes() throws IOException {
        return sendCommandString("getCameraMethodTypes", new JSONArray());
    }

    // Avcontent APIs

    /**
     * Calls getMethodTypes API of AvContent service to the target server.
     * Request JSON data is such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getMethodTypes",
     *   "params": ["1.0"],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject getAvcontentMethodTypes() throws IOException {
        return sendCommandString("getAvcontentMethodTypes", new JSONArray());
    }

    /**
     * Calls getSchemeList API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSchemeList",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSchemeList() throws IOException {
        return sendCommandString("getSchemeList", new JSONArray());
    }

    /**
     * Calls getSourceList API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSourceList",
     *   "params": [{
     *      "scheme": "storage"
     *      }],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param scheme target scheme to get source
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSourceList(String scheme) throws IOException {
        JSONArray param = new JSONArray();
        param.put(scheme);
        return sendCommandString("getSourceList", param);
    }

    /**
     * Calls getContentList API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getContentList",
     *   "params": [{
     *      "sort" : "ascending"
     *      "view": "date"
     *      "uri": "storage:memoryCard1"
     *      }],
     *   "id": 2,
     *   "version": "1.3"
     * }
     * </pre>
     *
     * @param params request JSON parameter of "params" object.
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getContentList(JSONArray params) throws IOException {
        return sendCommandString("getContentList", new JSONArray());
    }

    /**
     * Calls setStreamingContent API to the target server. Request JSON data is
     * such like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "setStreamingContent",
     *   "params": [
     *      "remotePlayType" : "simpleStreaming"
     *      "uri": "image:content?contentId=01006"
     *      ],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @param uri streaming contents uri
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setStreamingContent(String uri) throws IOException {
        JSONArray param = new JSONArray();
        param.put(uri);
        return sendCommandString("setStreamingContent", param);
    }

    /**
     * Calls startStreaming API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startStreaming",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject startStreaming() throws IOException {
        return sendCommandString("startStreaming", new JSONArray());
    }

    /**
     * Calls stopStreaming API to the target server. Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "stopStreaming",
     *   "params": [],
     *   "id": 2,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject stopStreaming() throws IOException {
        return sendCommandString("stopStreaming", new JSONArray());
    }

    // static method

    /**
     * Parse JSON and return whether it has error or not.
     *
     * @param replyJson JSON object to check
     * @return return true if JSON has error. otherwise return false.
     */
    public static boolean isErrorReply(JSONObject replyJson) {
        boolean hasError = (replyJson != null && replyJson.has("error"));
        return hasError;
    }

    /**
     * Calls startLiveviewWithSize API to the target server.  Starts the live with a specific size
     * M = VGA
     * L = XVGA
     * Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "startLiveviewWithSize",
     *   "params": [liveViewSize],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject startLiveviewWithSize(String liveViewSize) throws IOException {
        return sendCommandString("startLiveviewWithSize", new JSONArray());
    }

    /**
     * Calls getLiveviewSize API to the target server.  Gets the current live view size
     * M = VGA
     * L = XVGA
     * Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getLiveviewSize",
     *   "params": [liveViewSize],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getLiveviewSize() throws IOException {
        return sendCommandString("getLiveviewSize", new JSONArray());
    }

    /**
     * Calls getSupportedLiveviewSize API to the target server.  Gets the available live view sizes from the Camera
     * M = VGA
     * L = XVGA
     * Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSupportedLiveviewSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSupportedLiveviewSize() throws IOException {
        return sendCommandString("getSupportedLiveviewSize", new JSONArray());
    }

    /**
     * Calls getSupportedLiveviewSize API to the target server.  get current liveview size and the available liveview sizes at the moment.
     * M = VGA
     * L = XVGA
     * Request JSON data is such
     * like as below.
     * <p/>
     * <pre>
     * {
     *   "method": "getAvailableLiveviewSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response: Current Liveview Size, A list of available sizes
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getAvailableLiveviewSize() throws IOException {
        return sendCommandString("getAvailableLiveviewSize", new JSONArray());
    }

    /**
     * Calls setTouchAFPosition API to the target server.  Sets the touch AF position
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "setTouchAFPosition",
     *   "params": [double xPos, double yPos],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response: responseJson(int, obj):
     * Touch: Focus on around touch area
     * Wide: Focus on over a wide range including touch area
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setTouchAFPosition(double xPos, double yPos) throws IOException {
        JSONArray param;
        try{
            param = new JSONArray();
            param.put(xPos).put(yPos);
        } catch (JSONException e) {
            throw new IOException(e);
        }
        return sendCommandString("setTouchAFPosition", param);
    }

    /**
     * Calls getTouchAFPosition API to the target server.  Gets the touch AF position
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "getTouchAFPosition",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSON data of response: responseJson(int, obj):
     * touchCoordinates is reserved and will return empty
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getTouchAFPosition() throws IOException {
        return sendCommandString("getTouchAFPosition", new JSONArray());
    }

    /**
     * Calls cancelTouchAFPosition API to the target server.  Cancels the touch AF position
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "cancelTouchAFPosition",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     * <p/>
     * touchCoordinates is reserved and will return empty
     *
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject cancelTouchAFPosition() throws IOException {
        return sendCommandString("cancelTouchAFPosition", new JSONArray());
    }

    /**
     * Calls setSelfTimer API to the target server.  sets the Time for the timer
     *
     * @param time - 0, 2 and 10 seconds
     *             Example JSON data is below.
     *             <p/>
     *             <pre>
     *             {
     *               "method": "setSelfTimer",
     *               "params": [int time],
     *               "id": 1,
     *               "version": "1.0"
     *             }
     *             </pre>
     * @return String - Integer 0 for success error code for failure
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setSelfTimer(int time) throws IOException {
        JSONArray param = new JSONArray();
        param.put(time);
        return sendCommandString("setSelfTimer", param);
    }

    /**
     * Calls getSelfTimer API to the target server.  Gets the Time for the timer
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSelfTimer",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Integer 0, 2, 10 seconds
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSelfTimer() throws IOException {
        return sendCommandString("getSelfTimer", new JSONArray());
    }

    /**
     * Calls getSupportedSelfTimer API to the target server.  Gets the Time for the timer
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "getSupportedSelfTimer",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - returns JSONArray of supported times.
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSupportedSelfTimer() throws IOException {
        return sendCommandString("getSupportedSelfTimer", new JSONArray());
    }

    /**
     * Calls getAvailableSelfTimer API to the target server.  Gets the Time for the timer
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "getAvailableSelfTimer",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - returns Current time setting and available time settings
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getAvailableSelfTimer() throws IOException {
        return sendCommandString("getAvailableSelfTimer", new JSONArray());
    }

    /**
     * Calls setExposureMode API to the target server.  Sets the Exposure Mode
     * Exposure Modes :
     * Program Auto
     * Aperture
     * Shutter
     * Manual
     * Intelligent Auto
     * Superior Auto
     * Example JSON data is below.
     * <p/>
     * <pre>
     * {
     *   "method": "setExposureMode",
     *   "params": [exposureMode],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - returns 0 for success otherwise error code
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setExposureMode(String exposureMode) throws IOException {
        JSONArray param = new JSONArray();
        param.put(exposureMode);
        return sendCommandString("setExposureMode", param);
    }
    /**
     * Calls getExposureMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getExposureMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Exposure Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getExposureMode() throws IOException {
        return sendCommandString("getExposureMode", new JSONArray());
    }

    /**
     * Calls getSupportedExposureMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedExposureMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Exposure Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedExposureMode() throws IOException {
        return sendCommandString("getSupportedExposureMode", new JSONArray());
    }

    /**
     * Calls getAvailableExposureMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableExposureMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Exposure Mode and a list of the supported Exposure Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableExposureMode() throws IOException {
        return sendCommandString("getAvailableExposureMode", new JSONArray());
    }

    /**
     * Calls setFocusMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setFocusMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Focus Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setFocusMode(String focusMode) throws IOException {
        JSONArray param = new JSONArray();
        param.put(focusMode);
        return sendCommandString("setFocusMode", param);
    }

    /**
     * Calls getFocusMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getFocusMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Focus Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFocusMode() throws IOException {
        return sendCommandString("getFocusMode", new JSONArray());
    }

    /**
     * Calls getSupportedFocusMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedFocusMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Focus Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFocusMode() throws IOException {
        return sendCommandString("getSupportedFocusMode", new JSONArray());
    }

    /**
     * Calls getAvailableFocusMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableFocusMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Focus Mode and a list of the supported Focus Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFocusMode() throws IOException {
        return sendCommandString("getAvailableFocusMode", new JSONArray());
    }

    /**
     * Calls setExposureCompensation API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setExposureCompensation",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Exposure Compensation
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setExposureCompensation(int exposureComp) throws IOException {
        JSONArray param = new JSONArray();
        param.put(exposureComp);
        return sendCommandString("setExposureCompensation", param);
    }

    /**
     * Calls getExposureCompensation API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getExposureCompensation",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Exposure Compensation
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getExposureCompensation() throws IOException {
        return sendCommandString("getExposureCompensation", new JSONArray());
    }

    /**
     * Calls getSupportedExposureCompensation API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedExposureCompensation",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Exposure Compensations
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedExposureCompensation() throws IOException {
        return sendCommandString("getSupportedExposureCompensation", new JSONArray());
    }

        /**
         * Calls getAvailableExposureCompensation API to the target server.
         * Example JSON data is below.
         *
         * <pre>
         * {
         *   "method": "getAvailableExposureCompensation",
         *   "params": [],
         *   "id": 1,
         *   "version": "1.0"
         * }
         * </pre>
         *
         * @return String - Returns a string of the current Exposure Compensation and a list of the supported Exposure Compensations
         * @throws IOException all errors and exception are wrapped by this
         *             Exception.
         */

        public JSONObject getAvailableExposureCompensation() throws IOException {
            return sendCommandString("getAvailableExposureCompensation", new JSONArray());
        }

        /**
         * Calls setFNumber API to the target server.
         * Example JSON data is below.
         *
         * <pre>
         * {
         *   "method": "setFNumber",
         *   "params": [],
         *   "id": 1,
         *   "version": "1.0"
         * }
         * </pre>
         *
         * @return String - Returns 0 on success or error code on failure for F Number
         * @throws IOException all errors and exception are wrapped by this
         *             Exception.
         */

    public JSONObject setFNumber(String fNumber) throws IOException {
        JSONArray param = new JSONArray();
        param.put(fNumber);
        return sendCommandString("setFNumber", param);
    }

    /**
     * Calls getFNumber API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getFNumber",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current F Number
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFNumber() throws IOException {
        return sendCommandString("getFNumber", new JSONArray());
    }

    /**
     * Calls getSupportedFNumber API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedFNumber",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported F Numbers
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFNumber() throws IOException {
        return sendCommandString("getSupportedFNumber", new JSONArray());
    }

    /**
     * Calls getAvailableFNumber API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableFNumber",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current F Number and a list of the supported F Numbers
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFNumber() throws IOException {
        return sendCommandString("getAvailableFNumber", new JSONArray());
    }

    /**
     * Calls setShutterSpeed API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setShutterSpeed",
     *   "params": ["1/2"],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Shutter Speed
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setShutterSpeed(String shutterSpeed) throws IOException {
        JSONArray param = new JSONArray();
        param.put(shutterSpeed);
        return sendCommandString("setShutterSpeed", param);
    }

    /**
     * Calls getShutterSpeed API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getShutterSpeed",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Shutter Speed
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getShutterSpeed() throws IOException {
        return sendCommandString("getShutterSpeed", new JSONArray());
    }

    /**
     * Calls getSupportedShutterSpeed API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedShutterSpeed",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Shutter Speeds
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedShutterSpeed() throws IOException {
        return sendCommandString("getSupportedShutterSpeed", new JSONArray());
    }

    /**
     * Calls getAvailableShutterSpeed API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableShutterSpeed",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Shutter Speed and a list of the supported Shutter Speeds
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableShutterSpeed() throws IOException {
        return sendCommandString("getAvailableShutterSpeed", new JSONArray());
    }

    /**
     * Calls setIsoSpeedRate API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setIsoSpeedRate",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Iso Speed Rate
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setIsoSpeedRate(String isoSpeed) throws IOException {
        JSONArray param = new JSONArray();
        param.put(isoSpeed);
        return sendCommandString("setIsoSpeedRate", param);
    }

    /**
     * Calls getIsoSpeedRate API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getIsoSpeedRate",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Iso Speed Rate
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getIsoSpeedRate() throws IOException {
        return sendCommandString("getIsoSpeedRate", new JSONArray());
    }

    /**
     * Calls getSupportedIsoSpeedRate API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedIsoSpeedRate",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Iso Speed Rates
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedIsoSpeedRate() throws IOException {
        return sendCommandString("getSupportedIsoSpeedRate", new JSONArray());
    }

    /**
     * Calls getAvailableIsoSpeedRate API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableIsoSpeedRate",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Iso Speed Rate and a list of the supported Iso Speed Rates
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableIsoSpeedRate() throws IOException {
        return sendCommandString("getAvailableIsoSpeedRate", new JSONArray());
    }

    /**
     * Calls setWhiteBalance API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setWhiteBalance",
     *   "params": ["Color Temperature", true, 2500],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for White Balance
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setWhiteBalance(String mode, boolean enabled, int colorTemp) throws IOException {
        JSONArray param = new JSONArray();
        param.put(mode).put(enabled).put(colorTemp);
        return sendCommandString("setWhiteBalance", param);
    }

    /**
     * Calls getWhiteBalance API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getWhiteBalance",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current White Balance
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getWhiteBalance() throws IOException {
        return sendCommandString("getWhiteBalance", new JSONArray());
    }

    /**
     * Calls getSupportedWhiteBalance API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedWhiteBalance",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported White Balances
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedWhiteBalance() throws IOException {
        return sendCommandString("getSupportedWhiteBalance", new JSONArray());
    }

    /**
     * Calls getAvailableWhiteBalance API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableWhiteBalance",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current White Balance and a list of the supported White Balances
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableWhiteBalance() throws IOException {
        return sendCommandString("getAvailableWhiteBalance", new JSONArray());
    }

    /**
     * Calls setProgramShift API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setProgramShift",
     *   "params": [1],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Program Shift
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setProgramShift(int shift) throws IOException {
        JSONArray param = new JSONArray();
        param.put(shift);
        return sendCommandString("setProgramShift", param);
    }

    /**
     * Calls getSupportedProgramShift API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedProgramShift",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Program Shifts
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedProgramShift() throws IOException {
        return sendCommandString("getSupportedProgramShift", new JSONArray());
    }

    /**
     * Calls setFlashMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setFlashMode",
     *   "params": ["OFF"],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Flash Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setFlashMode(String flashMode) throws IOException {
        JSONArray param = new JSONArray();
        param.put(flashMode);
        return sendCommandString("setFlashMode", param);
    }

    /**
     * Calls getFlashMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getFlashMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Flash Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFlashMode() throws IOException {
        return sendCommandString("getFlashMode", new JSONArray());
    }

    /**
     * Calls getSupportedFlashMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedFlashMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Flash Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFlashMode() throws IOException {
        return sendCommandString("getSupportedFlashMode", new JSONArray());
    }

    /**
     * Calls getAvailableFlashMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailableFlashMode",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Flash Mode and a list of the supported Flash Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFlashMode() throws IOException {
        return sendCommandString("getAvailableFlashMode", new JSONArray());
    }

    /**
     * Calls setPostviewImageSize API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setPostviewImageSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns 0 on success or error code on failure for Postview Image Size
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setPostviewImageSize() throws IOException {
        return sendCommandString("setPostviewImageSize", new JSONArray());
    }

    /**
     * Calls getPostviewImageSize API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getPostviewImageSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Postview Image Size
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getPostviewImageSize() throws IOException {
        return sendCommandString("getPostviewImageSize", new JSONArray());
    }

    /**
     * Calls getSupportedPostviewImageSize API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getSupportedPostviewImageSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a list of the supported Postview Image Sizes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedPostviewImageSize() throws IOException {
        return sendCommandString("getSupportedPostviewImageSize", new JSONArray());
    }

    /**
     * Calls getAvailablePostviewImageSize API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getAvailablePostviewImageSize",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Postview Image Size and a list of the supported Postview Image Sizes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailablePostviewImageSize() throws IOException {
        return sendCommandString("getAvailablePostviewImageSize", new JSONArray());
    }

    /**
     * Calls getVersions API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getVersions",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Versions
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getVersions() throws IOException {
        return sendCommandString("getVersions", new JSONArray());
    }

    /**
     * Calls getMethodTypes API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getMethodTypes",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Method Types
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getMethodTypes() throws IOException {
        return sendCommandString("getMethodTypes", new JSONArray());
    }

    /**
     * Calls getEvent (v1.0) API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getEvent (v1.0)",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Event (v1.0)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_0(boolean longPollingFlag) throws IOException {
        JSONArray param = new JSONArray();
        param.put(longPollingFlag);
        return sendCommandString("getEventv1_0", param);
    }

    /**
     * Calls getEvent (v1.1) API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getEvent (v1.1)",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Event (v1.1)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_1(boolean longPollingFlag) throws IOException {
        JSONArray param = new JSONArray();
        param.put(longPollingFlag);
        return sendCommandString("getEventv1_1", param);
    }

    /**
     * Calls getEvent (v1.2) API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getEvent (v1.2)",
     *   "params": [],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return String - Returns a string of the current Event (v1.2)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_2(boolean longPollingFlag) throws IOException {
        JSONArray param = new JSONArray();
        param.put(longPollingFlag);
        return sendCommandString("getEventv1_2", param);
    }
}
