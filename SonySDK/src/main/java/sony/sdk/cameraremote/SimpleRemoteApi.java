/*
 * Copyright 2014 Sony Corporation
 * Modified by stackoverflow user Keysersoze
 */

package sony.sdk.cameraremote;

import sony.sdk.cameraremote.ServerDevice.ApiService;
import sony.sdk.cameraremote.utils.SimpleHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * Simple Camera Remote API wrapper class. (JSON based API <--> Java API)
 */
public class SimpleRemoteApi {

    private static final String TAG = SimpleRemoteApi.class.getSimpleName();

    // If you'd like to suppress detailed log output, change this value into
    // false.
    private static final boolean FULL_LOG = true;

    // API server device you want to send requests.
    private ServerDevice mTargetServer;

    // Request ID of API calling. This will be counted up by each API calling.
    private int mRequestId;

    private static SimpleRemoteApi simpleRemoteApiInstance = null;

    /**
     * Constructor.
     *
     * @param target server device of Remote API
     */
    private SimpleRemoteApi() {
        mRequestId = 1;
    }

    public static SimpleRemoteApi getInstance(){
        if(simpleRemoteApiInstance == null) {
            simpleRemoteApiInstance = new SimpleRemoteApi();
        }
        return simpleRemoteApiInstance;
    }

    public void init(ServerDevice target) {
        mTargetServer = target;

    }

    /**
     * Retrieves Action List URL from Server information.
     *
     * @param service
     * @return
     * @throws IOException
     */
    private String findActionListUrl(String service) throws IOException {
        List<ApiService> services = mTargetServer.getApiServices();
        for (ApiService apiService : services) {
            if (apiService.getName().equals(service)) {
                return apiService.getActionListUrl();
            }
        }
        throw new IOException("actionUrl not found. service : " + service);
    }

    /**
     * Request ID. Counted up after calling.
     *
     * @return
     */
    private int id() {
        return mRequestId++;
    }

    // Output a log line.
    private void log(String msg) {
        if (FULL_LOG) {
            Log.d(TAG, msg);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableApiList")
                            .put("params", new JSONArray()).put("id", id())
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getApplicationInfo") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getShootMode").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * "still" Still image shoot mode
     * "movie" Movie shoot mode
     * "audio" Audio shoot mode
     * "intervalstill" Interval still shoot mode
     * "looprec" Loop recording shoot mode
     *
     * @param shootMode shoot mode (ex. "still")
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject setShootMode(String shootMode) throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setShootMode") //
                            .put("params", new JSONArray().put(shootMode)) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableShootMode") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedShootMode") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startContShooting") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopContShooting") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startBulbShooting") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopBulbShooting") //
                            .put("params", new JSONArray()).put("id", id()) //
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startLiveview").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopLiveview").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startRecMode").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopRecMode").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "actHalfPressShutter").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "actTakePicture").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    public JSONObject actTakePicture(int timeout) throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "actTakePicture").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString(), timeout);
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        } catch (IOException ioException) {
            throw new IOException(ioException);
        }
    }

    public JSONObject awaitTakePicture(int timeout) throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "awaitTakePicture").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString(), timeout);
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        } catch (IOException ioException) {
            throw new IOException(ioException);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startMovieRec").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopMovieRec").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "actZoom") //
                            .put("params", new JSONArray().put(direction).put(movement)) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getEvent") //
                            .put("params", new JSONArray().put(longPollingFlag)) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;
            int longPollingTimeout = (longPollingFlag) ? 20000 : 8000; // msec

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString(),
                    longPollingTimeout);
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * "Remote Shooting" Shooting function
     * "Contents Transfer" Transferring images function
     *
     * @param cameraFunction camera function to set
     * @return JSON data of response
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */
    public JSONObject setCameraFunction(String cameraFunction) throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setCameraFunction") //
                            .put("params", new JSONArray().put(cameraFunction)) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getMethodTypes") //
                            .put("params", new JSONArray().put("")) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {
            String url = findActionListUrl(service) + "/" + service;
            JSONObject requestJson =
                    new JSONObject().put("method", "getMethodTypes") //
                            .put("params", new JSONArray().put("")) //
                            .put("id", id()).put("version", "1.0"); //

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSchemeList").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {

            JSONObject params = new JSONObject().put("scheme", scheme);
            JSONObject requestJson =
                    new JSONObject().put("method", "getSourceList") //
                            .put("params", new JSONArray().put(0, params)) //
                            .put("version", "1.0").put("id", id());

            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {

            JSONObject requestJson =
                    new JSONObject().put("method", "getContentList").put("params", params) //
                            .put("version", "1.3").put("id", id());

            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {

            JSONObject params = new JSONObject().put("remotePlayType", "simpleStreaming").put(
                    "uri", uri);
            JSONObject requestJson =
                    new JSONObject().put("method", "setStreamingContent") //
                            .put("params", new JSONArray().put(0, params)) //
                            .put("version", "1.0").put("id", id());

            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "startStreaming").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0").put("id", id());
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "avContent";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "stopStreaming").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONArray params = new JSONArray().put(liveViewSize);
            JSONObject requestJson =
                    new JSONObject().put("method", "startLiveviewWithSize").put("params", params) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getLiveviewSize").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedLiveviewSize").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableLiveviewSize").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {

            JSONArray params = new JSONArray().put(xPos);
            params.put(yPos);
            JSONObject requestJson =
                    new JSONObject().put("method", "setTouchAFPosition").put("params", params) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getTouchAFPosition").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "cancelTouchAFPosition").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Integer 0 for success error code for failure
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setSelfTimer(int time) throws IOException {
        String service = "camera";
        try {
            JSONArray param = new JSONArray().put(time);
            JSONObject requestJson =
                    new JSONObject().put("method", "setSelfTimer").put("params", param) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Integer 0, 2, 10 seconds
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSelfTimer() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSelfTimer").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - returns JSONArray of supported times.
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getSupportedSelfTimer() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedSelfTimer").put("params", new JSONArray()) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - returns Current time setting and available time settings
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject getAvailableSelfTimer() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableSelfTimer").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - returns 0 for success otherwise error code
     * @throws IOException all errors and exception are wrapped by this
     *                     Exception.
     */

    public JSONObject setExposureMode(String exposureMode) throws IOException {
        String service = "camera";
        try {
            JSONArray exposureModeSetting = new JSONArray().put(exposureMode);
            JSONObject requestJson =
                    new JSONObject().put("method", "setExposureMode").put("params", exposureModeSetting) //
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Exposure Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getExposureMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getExposureMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Exposure Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedExposureMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedExposureMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Exposure Mode and a list of the supported Exposure Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableExposureMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableExposureMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setFocusMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setFocusMode",
     *   "params": [String focusMode],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     * "AF-S" Single AF
     * "AF-C" Continuous AF
     * "DMF" Direct Manual Focus
     * "MF" Manual Focus
     *
     *
     * @return JSONObject - Returns 0 on success or error code on failure for Focus Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setFocusMode(String focusMode) throws IOException {
        String service = "camera";
        try {
            JSONArray focusModeSetting = new JSONArray();
            focusModeSetting.put(focusMode);
            JSONObject requestJson =
                    new JSONObject().put("method", "setFocusMode").put("params", focusModeSetting)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Focus Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFocusMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getFocusMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Focus Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFocusMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedFocusMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Focus Mode and a list of the supported Focus Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFocusMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableFocusMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns 0 on success or error code on failure for Exposure Compensation
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setExposureCompensation(int exposureComp) throws IOException {
        String service = "camera";
        try {
            JSONArray params = new JSONArray().put(exposureComp);
            JSONObject requestJson =
                    new JSONObject().put("method", "setExposureCompensation").put("params", params)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Exposure Compensation
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getExposureCompensation() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getExposureCompensation").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Exposure Compensations
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedExposureCompensation() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedExposureCompensation").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Exposure Compensation and a list of the supported Exposure Compensations
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableExposureCompensation() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableExposureCompensation").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns 0 on success or error code on failure for F Number
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setFNumber(String fNumber) throws IOException {
        String service = "camera";
        try {
            JSONArray params = new JSONArray().put(fNumber);
            JSONObject requestJson =
                    new JSONObject().put("method", "setFNumber").put("params", params)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current F Number
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFNumber() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getFNumber").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported F Numbers
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFNumber() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedFNumber").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current F Number and a list of the supported F Numbers
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFNumber() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableFNumber").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns 0 on success or error code on failure for Shutter Speed
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setShutterSpeed(String shutterSpeed) throws IOException {
        String service = "camera";
        try {
            JSONArray params = new JSONArray().put(shutterSpeed);
            JSONObject requestJson =
                    new JSONObject().put("method", "setShutterSpeed").put("params", params)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Shutter Speed
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getShutterSpeed() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getShutterSpeed").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Shutter Speeds
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedShutterSpeed() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedShutterSpeed").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Shutter Speed and a list of the supported Shutter Speeds
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableShutterSpeed() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableShutterSpeed").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setIsoSpeedRate API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setIsoSpeedRate",
     *   "params": [String isoSpeedRate],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSONObject - Returns 0 on success or error code on failure for Iso Speed Rate
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setIsoSpeedRate(String isoSpeedRate) throws IOException {
        String service = "camera";
        JSONArray isoSpeed = new JSONArray();
        isoSpeed.put(isoSpeedRate);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setIsoSpeedRate")
                            .put("params", isoSpeed)
                            .put("id", id())
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Iso Speed Rate
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getIsoSpeedRate() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getIsoSpeedRate").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Iso Speed Rates
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedIsoSpeedRate() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedIsoSpeedRate").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Iso Speed Rate and a list of the supported Iso Speed Rates
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableIsoSpeedRate() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableIsoSpeedRate").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setWhiteBalance API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setWhiteBalance",
     *   "params": [String whiteBalance, boolean enableColorTemp, int colorTemp],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSONObject - Returns 0 on success or error code on failure for White Balance
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setWhiteBalance(String whiteBalance, boolean enableColorTemp, int colorTemp) throws IOException {
        String service = "camera";
        JSONArray whiteBalanceSettings = new JSONArray();
        whiteBalanceSettings.put(whiteBalance);
        whiteBalanceSettings.put(enableColorTemp);
        whiteBalanceSettings.put(colorTemp);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setWhiteBalance").put("params", whiteBalanceSettings)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current White Balance
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getWhiteBalance() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getWhiteBalance").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported White Balances
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedWhiteBalance() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedWhiteBalance").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current White Balance and a list of the supported White Balances
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableWhiteBalance() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableWhiteBalance").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setProgramShift API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setProgramShift",
     *   "params": [String programShiftAmount],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * @return JSONObject - Returns 0 on success or error code on failure for Program Shift
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setProgramShift(String programShiftAmount) throws IOException {
        String service = "camera";
        JSONArray programShiftSettings = new JSONArray();
        programShiftSettings.put(programShiftAmount);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setProgramShift").put("params", programShiftSettings)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Program Shifts
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedProgramShift() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedProgramShift")
                            .put("params", new JSONArray())
                            .put("id", id())
                            .put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setFlashMode API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setFlashMode",
     *   "params": [String flashMode],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * "off" OFF
     * "auto" Auto flash
     * "on" Forced flash
     * "slowSync" Slow synchro
     * "rearSync" Rear synchro
     * "wireless" Wireless
     *
     * @return JSONObject - Returns 0 on success or error code on failure for Flash Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setFlashMode(String flashMode) throws IOException {
        String service = "camera";
        JSONArray flashModeSettings = new JSONArray();
        flashModeSettings.put(flashMode);

        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setFlashMode").put("params", flashModeSettings)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Flash Mode
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getFlashMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getFlashMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Flash Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedFlashMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedFlashMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Flash Mode and a list of the supported Flash Modes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailableFlashMode() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailableFlashMode").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls setPostviewImageSize API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "setPostviewImageSize",
     *   "params": [String postViewImageSize],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * "Original" Original size
     * "2M" 2M-pixel size (the actual size depends on camera models.)
     *
     * @return JSONObject - Returns 0 on success or error code on failure for Postview Image Size
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject setPostviewImageSize(String postViewImageSize) throws IOException {
        String service = "camera";
        JSONArray postViewImageSizeSettings = new JSONArray();
        postViewImageSizeSettings.put(postViewImageSize);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "setPostviewImageSize").put("params", postViewImageSizeSettings)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Postview Image Size
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getPostviewImageSize() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getPostviewImageSize").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a list of the supported Postview Image Sizes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getSupportedPostviewImageSize() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getSupportedPostviewImageSize").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Postview Image Size and a list of the supported Postview Image Sizes
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getAvailablePostviewImageSize() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getAvailablePostviewImageSize").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Versions
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getVersions() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getVersions").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * @return JSONObject - Returns a string of the current Method Types
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getMethodTypes() throws IOException {
        String service = "camera";
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getMethodTypes").put("params", new JSONArray())
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls getEvent (v1.0) API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getEvent (v1.0)",
     *   "params": [String poll],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * Long polling flag
     * true: Callback when timeout or change point detection.
     * false: Callback immediately.
     *
     * @return JSONObject - Returns a string of the current Event (v1.0)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_0(boolean poll) throws IOException {
        String service = "camera";
        JSONArray pollSettings = new JSONArray();
        pollSettings.put(poll);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getEvent").put("params", pollSettings)
                            .put("id", id()).put("version", "1.0");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * Calls getEvent (v1.1) API to the target server.
     * Example JSON data is below.
     *
     * <pre>
     * {
     *   "method": "getEvent (v1.1)",
     *   "params": [String poll],
     *   "id": 1,
     *   "version": "1.0"
     * }
     * </pre>
     *
     * Long polling flag
     * true: Callback when timeout or change point detection.
     * false: Callback immediately.
     *
     * @return JSONObject - Returns a string of the current Event (v1.1)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_1(boolean poll) throws IOException {
        String service = "camera";
        try {
            JSONArray pollSettings = new JSONArray();
            pollSettings.put(poll);
            JSONObject requestJson =
                    new JSONObject().put("method", "getEvent").put("params", pollSettings)
                            .put("id", id()).put("version", "1.1");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
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
     * Long polling flag
     * true: Callback when timeout or change point detection.
     * false: Callback immediately.
     *
     * @return JSONObject - Returns a string of the current Event (v1.2)
     * @throws IOException all errors and exception are wrapped by this
     *             Exception.
     */

    public JSONObject getEventv1_2(boolean poll) throws IOException {
        String service = "camera";
        JSONArray pollSettings = new JSONArray();
        pollSettings.put(poll);
        try {
            JSONObject requestJson =
                    new JSONObject().put("method", "getEvent").put("params", pollSettings)
                            .put("id", id()).put("version", "1.2");
            String url = findActionListUrl(service) + "/" + service;

            log("Request:  " + requestJson.toString());
            String responseJson = SimpleHttpClient.httpPost(url, requestJson.toString());
            log("Response: " + responseJson);
            return new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
}