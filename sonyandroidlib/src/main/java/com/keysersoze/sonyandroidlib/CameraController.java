package com.keysersoze.sonyandroidlib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import keysersoze.com.BracketCameraControllerAPI;
import sony.sdk.cameraremote.ServerDevice;
import sony.sdk.cameraremote.SimpleCameraEventObserver;
import sony.sdk.cameraremote.SimpleRemoteApi;
import sony.sdk.cameraremote.SimpleSsdpClient;

import static com.keysersoze.sonyandroidlib.IsSupportedUtil.isShootingStatus;

/**
 * Created by aaron on 5/29/15.
 */
public class CameraController implements BracketCameraControllerAPI {

    private boolean connectionStatus = false;
    private String cameraAddress;

    private static final String TAG = "CameraController";

    private ServerDevice serverDevice;
    private static SimpleCameraEventObserver.ChangeListener mEventListener;
    private static SimpleRemoteApi mRemoteApi;
    private static SimpleCameraEventObserver mEventObserver;
    private static final Set<String> mSupportedApiSet = new HashSet<String>();
    private static final Set<String> mAvailableApiSet = new HashSet<String>();
    private String cameraMode;
    private String liveViewUrl = null;
    private static CameraConnectionHandler connectionHandler;
    private Context context;
    private SimpleSsdpClient ssdpClient;
    private List<ResultCallback> resultCallbacks;
    private List<CameraStateChangeCallback> stateChangeCallbacks;
    private int currentCamerastate = BracketCameraControllerAPI.NOT_READY;
    private int previousCamerastate = BracketCameraControllerAPI.NOT_READY;
    private long timeOfPictureTaken = 0;
    private static Handler awaitHandler;
    private HandlerThread awaitThread = null;
    private static Handler bulbHandler;
    private static Handler bulbPostHandler;
    private HandlerThread bulbThread = null;

    private final String GET_FSTOP_API = "getAvailableFNumber";
    private final String GET_SHUTTER_SPEED_API = "getAvailableShutterSpeed";
    private final String GET_ISO_API = "getAvailableIsoSpeedRate";
    private final String GET_TAKE_PHOTO_API = "actTakePicture";
    private final String AWAIT_TAKE_PICTURE_API = "awaitTakePicture";
    private boolean photoCompleteFlag = false;

    private String globalShutterSpeed;
    private int timeout;

    private boolean apisReady = false;

    public CameraController(Context context, CameraConnectionHandler connectionHandler) {
        this.context = context;
        this.connectionHandler = connectionHandler;
        resultCallbacks = new ArrayList<>();
        stateChangeCallbacks = new ArrayList<>();
        awaitThread = new HandlerThread("PHOTO_THREAD");
        awaitThread.start();

        bulbThread = new HandlerThread("PHOTO_THREAD");
        bulbThread.start();

        awaitHandler = new Handler(awaitThread.getLooper()) {
            @Override
            public void handleMessage(Message awaitMessage) {
                try {
                    Bundle dataBundle = awaitMessage.getData();
                    int timeout = dataBundle.getInt(BracketCameraControllerAPI.AWAIT_TAKE_PICTURE);

                    JSONObject result = mRemoteApi.awaitTakePicture(timeout);
                    sendResult(result, BracketCameraControllerAPI.AWAIT_TAKE_PICTURE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        bulbHandler = new Handler(bulbThread.getLooper()) {
            @Override
            public void handleMessage(Message bulbMessage) {
                try {
                    Bundle dataBundle = bulbMessage.getData();
                    int timeout = dataBundle.getInt(BracketCameraControllerAPI.AWAIT_TAKE_PICTURE);

                    JSONObject result = mRemoteApi.stopBulbShooting(timeout);
                    sendResult(result, BracketCameraControllerAPI.AWAIT_TAKE_PICTURE);
                    photoCompleteFlag = true;
                    bulbPostHandler.sendEmptyMessage(201);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        bulbPostHandler = new Handler(bulbThread.getLooper()) {
            @Override
            public void handleMessage(Message bulbMessage) {
                try {
                    while (photoCompleteFlag) {

                        try {
                            Thread.sleep(5000);

                            updateCameraState();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /*
     * Interface for SSDP
     */
    public void cameraDeviceFound(ServerDevice serverDevice) {
        final String deviceAddress = serverDevice.getDDUrl();
        cameraAddress = deviceAddress;
        this.serverDevice = serverDevice;
        mRemoteApi = SimpleRemoteApi.getInstance();
        mRemoteApi.init(serverDevice);

        mEventListener = new SimpleCameraEventObserver.ChangeListenerTmpl() {

            @Override
            public void onShootModeChanged(String shootMode) {
                Log.d(TAG, "onShootModeChanged() called: " + shootMode);
            }

            @Override
            public void onCameraStatusChanged(String status) {
                Log.d(TAG, "onCameraStatusChanged()" + status);
                currentCamerastate = convertCameraState(status);
                for(CameraStateChangeCallback callback : stateChangeCallbacks) {
                    callback.onCameraStateChange(currentCamerastate);
                }
                previousCamerastate = currentCamerastate;

                checkoutPhotoComplete();

            }

            @Override
            public void onApiListModified(List<String> apis) {
                if(apis.contains(GET_FSTOP_API)
                        && apis.contains(GET_ISO_API)
                        &&apis.contains(GET_SHUTTER_SPEED_API)
                        &&apis.contains(GET_TAKE_PHOTO_API) && !apisReady){
                    connectionHandler.onCameraReady();
                    mEventObserver.stop();
                    apisReady = true;
                }
            }

            @Override
            public void onZoomPositionChanged(int zoomPosition) {
                Log.d(TAG, "onZoomPositionChanged() called = " + zoomPosition);

            }

            @Override
            public void onLiveviewStatusChanged(boolean status) {
                Log.d(TAG, "onLiveviewStatusChanged() called = " + status);
            }

            @Override
            public void onStorageIdChanged(String storageId) {
                Log.d(TAG, "onStorageIdChanged() called: " + storageId);
            }
        };

        mEventObserver = new SimpleCameraEventObserver(context, mRemoteApi);
        mEventObserver.activate();
    }

    public void onFinished() {
        openConnection();

    }

    public void onErrorFinished() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Error!");
            }
        });
    }

    @Override
    public String startLiveview() {
        String liveViewUrl = null;

        ExecutorService service =  Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String viewUrl = null;
                try {
                    JSONObject replyJson = null;
                    replyJson = mRemoteApi.startLiveviewWithSize("L");

                    if (!SimpleRemoteApi.isErrorReply(replyJson)) {
                        JSONArray resultsObj = replyJson.getJSONArray("result");
                        if (1 <= resultsObj.length()) {
                            // Obtain liveview URL from the result.
                            viewUrl = resultsObj.getString(0);
                        }
                    }
                } catch (IOException e) {
                    Log.w(TAG, "startLiveview IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.w(TAG, "startLiveview JSONException: " + e.getMessage());
                }
                return viewUrl;
            }
        });

        try {
            liveViewUrl = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return liveViewUrl;
    }

    @Override
    public void stopLiveview() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mRemoteApi.stopLiveview();
                } catch (IOException e) {
                    Log.w(TAG, "stopLiveview IOException: " + e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void setIso(String isoSpeed) {
        JSONObject result = null;
        try {
            result = mRemoteApi.setIsoSpeedRate(isoSpeed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendResult(result, SET_ISO_RESULT);
    }

    @Override
    public void setShutterSpeed(String shutterSpeed) {
        JSONObject result = null;
        this.globalShutterSpeed = shutterSpeed;
        String setShutterSpeed = shutterSpeed;
        try {
            int shutterSpeedInt = SonyCameraControllerUtil.getNormalizedTimeInSeconds(shutterSpeed);
            if(shutterSpeedInt > 30){
                setShutterSpeed = "BULB";
            }
            result = mRemoteApi.setShutterSpeed(setShutterSpeed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendResult(result, SET_SHUTTER_SPEED_RESULT);
    }

    @Override
    public void setFstop(String fstop) {
        JSONObject result = null;
        try {
            result = mRemoteApi.setFNumber(fstop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendResult(result, SET_FSTOP_RESULT);
    }

    @Override
    public void getIso() throws IOException{
        JSONObject isoResult = null;
        isoResult = mRemoteApi.getIsoSpeedRate();
        sendResult(isoResult, BracketCameraControllerAPI.ISO_RESULT);
    }

    @Override
    public void getAvailableIso() throws IOException{
        JSONObject supportedIsoResult = null;
        supportedIsoResult = mRemoteApi.getAvailableIsoSpeedRate();
        sendResult(supportedIsoResult, BracketCameraControllerAPI.SUPPORTED_ISO_RESULT);
    }

    @Override
    public void getShutterSpeed() throws IOException{
        JSONObject shutterSpeedResult = null;
        shutterSpeedResult = mRemoteApi.getShutterSpeed();
        sendResult(shutterSpeedResult, BracketCameraControllerAPI.SHUTTER_SPEED_RESULT);
    }

    @Override
    public void getAvailableShutterSpeed() throws IOException{
        JSONObject supportedShutterSpeedResult = null;
        String supportedShutterSpeedResultString = null;
        supportedShutterSpeedResult = mRemoteApi.getAvailableShutterSpeed();
        sendResult(supportedShutterSpeedResult, BracketCameraControllerAPI.SUPPORTED_SHUTTER_SPEED_RESULT);
    }


    @Override
    public void getFstop() throws IOException{
        JSONObject fstopResult = null;
        fstopResult = mRemoteApi.getFNumber();
        sendResult(fstopResult, BracketCameraControllerAPI.FSTOP_RESULT);
    }

    @Override
    public void getAvailableFstop() throws IOException{
        JSONObject supportedFStopResult = null;
        supportedFStopResult = mRemoteApi.getAvailableFNumber();
        sendResult(supportedFStopResult, BracketCameraControllerAPI.SUPPORTED_FSTOP_RESULT);
    }

    private void sendResult(JSONObject result, String prefix) {
        String resultString = null;
        StringBuilder stringBuilder = new StringBuilder();
        if(result != null){
            resultString = SonyCameraControllerUtil.parseSingleResult(result);
            Log.i(TAG, resultString);
            if(resultString != null && resultString.contains("postview")){

                if(prefix.equals(BracketCameraControllerAPI.AWAIT_TAKE_PICTURE)){
                    photoCompleteFlag = true;
                    try {
                        updateCameraState();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(currentCamerastate == BracketCameraControllerAPI.IDLE){
                    resultString = BracketCameraControllerAPI.PHOTO_COMPLETE;
                    awaitHandler.removeCallbacksAndMessages(null);
                    photoCompleteFlag = false;
                }else {
                    photoCompleteFlag = true;
                    awaitHandler.removeCallbacksAndMessages(null);
                }

            }else if(resultString.contains("40403")){
                try {
                    awaitTakePicture(timeout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if((prefix.equals(BracketCameraControllerAPI.SUPPORTED_ISO_RESULT)
                        || prefix.equals(BracketCameraControllerAPI.SUPPORTED_SHUTTER_SPEED_RESULT)
                        || prefix.equals(BracketCameraControllerAPI.SUPPORTED_FSTOP_RESULT))
                        && !resultString.contains("--")){
                    resultString = resultString.substring(resultString.indexOf(",") + 1);
                }
            }
            stringBuilder.append(prefix);
            stringBuilder.append(",");
            stringBuilder.append(resultString);
        }

        resultString = stringBuilder.toString();
        resultString = resultString.replace("[", "");
        resultString = resultString.replace("]", "");
        resultString = resultString.replace("\"", "");
        resultString = resultString.replace("\\", "");
        resultString = resultString.replace("BULB", "");

        for (ResultCallback callback : resultCallbacks) {
            callback.resultCallback(resultString);
        }
    }

    private void checkoutPhotoComplete(){
        //We dont know if the state change or the photo complete will trigger first.
        //Use a flag here and in the sendwait(5000);Result to check and reset the flag to false when finished.
        if(photoCompleteFlag && currentCamerastate == BracketCameraControllerAPI.IDLE){
            Log.i(TAG, "Photo Is complete");
            for (ResultCallback callback : resultCallbacks) {
                callback.resultCallback(BracketCameraControllerAPI.PHOTO_COMPLETE);
            }
            photoCompleteFlag = false;
        }
    }

    @Override
    public int getCameraState(){
        return currentCamerastate;
    }

    @Override
    public void updateCameraState() throws IOException{
        JSONArray supportedVersions;
        String latestVersion = "1.0";
        Log.i(TAG, "running getEvent");
        try {
            JSONObject versionResult = mRemoteApi.getVersions();
            supportedVersions = versionResult.getJSONArray("result");
            JSONArray eventJsonArray = supportedVersions.getJSONArray(0);
            latestVersion = (String) eventJsonArray.get(eventJsonArray.length()-1);

        } catch (IOException e) {
            throw e;
        } catch (JSONException e) {
            throw new IOException();
        }

        JSONObject eventObject = null;
        Log.i(TAG, "Getting camera status: " + latestVersion);
        try {
            switch (latestVersion) {
                case "1.1":
                    eventObject = mRemoteApi.getEventv1_1(false);
                    break;
                case "1.2":
                    eventObject = mRemoteApi.getEventv1_2(false);
                    break;
                case "1.3":
                    eventObject = mRemoteApi.getEventv1_2(false);
                    break;
                case "1.4":
                    eventObject = mRemoteApi.getEventv1_2(false);
                    break;
                default:
                    eventObject = mRemoteApi.getEventv1_0(false);
                    break;
            }
        }catch (IOException e) {

        }

        String stringCameraStatus = SonyCameraControllerUtil.parseCameraStatus(eventObject);
        currentCamerastate = convertCameraState(stringCameraStatus);
        checkoutPhotoComplete();
        for(CameraStateChangeCallback callback : stateChangeCallbacks) {
            callback.onCameraStateChange(currentCamerastate);
        }
    }

    /**
     * Check the shooting mode
     */
    private void checkShootingMode(){

        JSONObject replyJson = null;

        // getAvailableApiList
        if (isShootingStatus(cameraMode)) {
            Log.d(TAG, "camera function is Remote Shooting.");

        } else {
            // set Listener
            startOpenConnectionAfterChangeCameraState();

            // set Camera function to Remote Shooting
            try {
                replyJson = mRemoteApi.setCameraFunction("Remote Shooting");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void startOpenConnectionAfterChangeCameraState() {
        Log.d(TAG, "startOpenConectiontAfterChangeCameraState() exec");

        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mEventObserver
                        .setEventChangeListener(new SimpleCameraEventObserver.ChangeListenerTmpl() {

                            @Override
                            public void onCameraStatusChanged(String status) {
                                Log.d(TAG, "onCameraStatusChanged:" + status);
                                if ("IDLE".equals(status) || "NotReady".equals(status)) {
                                    openConnection();
                                }
                                refreshUi();
                            }

                            @Override
                            public void onShootModeChanged(String shootMode) {
                                refreshUi();
                            }

                            @Override
                            public void onStorageIdChanged(String storageId) {
                                refreshUi();
                            }
                        });

                mEventObserver.start();
            }
        });
    }

    /**
     * Refresh UI appearance along with current "cameraStatus" and "shootMode".
     */
    private void refreshUi() {
        String cameraStatus = mEventObserver.getCameraStatus();
        String shootMode = mEventObserver.getShootMode();
        List<String> availableShootModes = mEventObserver.getAvailableShootModes();
    }


    /**
     * Open connection to the camera device to start monitoring Camera events
     * and s        if(supportedShutterSpeedResult != null){
            supportedShutterSpeedResultString = SonyCameraControllerUtil.parseSingleResult(supportedShutterSpeedResult);
            Log.i(TAG, "Result Received String: " + restultString);
        }
        for (ResultCallback callback : resultCallbacks) {
            callback.resultCallback(supportedShutterSpeedResultString);
        }howing liveview.
     */

    @Override
    public void openConnection() {
        if(ssdpClient == null){
            ssdpClient = new SimpleSsdpClient();
        }
        SimpleSsdpClient.SearchResultHandler searchResultHandler = new SimpleSsdpClient.SearchResultHandler() {
            @Override
            public void onDeviceFound(ServerDevice serverDevice) {
                mRemoteApi = SimpleRemoteApi.getInstance();
                mRemoteApi.init(serverDevice);
                cameraDeviceFound(serverDevice);
            }

            @Override
            public void onFinished() {
                mEventObserver.setEventChangeListener(mEventListener);
                Log.d(TAG, "openConnection(): exec.");

                try {
                    JSONObject replyJson = null;

                    // getAvailableApiList
                    replyJson = mRemoteApi.getAvailableApiList();
                    loadAvailableCameraApiList(replyJson);

                    // check version of the server device
                    if (IsSupportedUtil.isCameraApiAvailable("getApplicationInfo", mAvailableApiSet)) {
                        Log.d(TAG, "openConnection(): getApplicationInfo()");
                        replyJson = mRemoteApi.getApplicationInfo();
/*                        if (!IsSupportedUtil.isSupportedServerVersion(replyJson)) {
                    DisplayHelper.toast(getApplicationContext(), //
                            R.string.msg_error_non_supported_device);
                    SampleCameraActivity.this.finish();
                    return;
                }*/
                    } else {
                        // never happens;
                        return;
                    }

                    // startRecMode if necessary.
                    if (IsSupportedUtil.isCameraApiAvailable("startRecMode", mAvailableApiSet)) {
                        Log.d(TAG, "openConnection(): startRecMode()");
                        replyJson = mRemoteApi.startRecMode();

                        // Call again.
                        replyJson = mRemoteApi.getAvailableApiList();
                        loadAvailableCameraApiList(replyJson);
                    }

                    // getEvent start
                    if (IsSupportedUtil.isCameraApiAvailable("getEvent", mAvailableApiSet)) {
                        Log.d(TAG, "openConnection(): EventObserver.start()");
                        mEventObserver.start();
                    }

                    Log.d(TAG, "openConnection(): completed.");
                    connectionHandler.onCameraConnected();


                } catch (IOException e) {
                    Log.w(TAG, "openConnection : IOException: " + e.getMessage());
                }
            }

            @Override
            public void onErrorFinished() {
                Log.w(TAG, "openConnection : Error Finished");

            }
        };
        ssdpClient.search(searchResultHandler);

    }

    /**
     * Retrieve a list of APIs that are supported by the target device.
     * Taken from the Sony Sample App
     * @param replyJson
     */
    private static void loadSupportedApiList(JSONObject replyJson) {
        synchronized (mSupportedApiSet) {
            try {
                JSONArray resultArrayJson = replyJson.getJSONArray("results");
                for (int i = 0; i < resultArrayJson.length(); i++) {
                    mSupportedApiSet.add(resultArrayJson.getJSONArray(i).getString(0));
                }
            } catch (JSONException e) {
                Log.w(TAG, "loadSupportedApiList: JSON format error.");
            }
        }
    }

    /**
     * Retrieve a list of APIs that are available at present.
     *
     * @param replyJson
     */
    private static void loadAvailableCameraApiList(JSONObject replyJson) {
        synchronized (mAvailableApiSet) {
            mAvailableApiSet.clear();
            try {
                JSONArray resultArrayJson = replyJson.getJSONArray("result");
                JSONArray apiListJson = resultArrayJson.getJSONArray(0);
                for (int i = 0; i < apiListJson.length(); i++) {
                    mAvailableApiSet.add(apiListJson.getString(i));
                }
            } catch (JSONException e) {
                Log.w(TAG, "loadAvailableCameraApiList: JSON format error.");
            }
        }
    }

    public Set<String> getApiSet() {
        return mAvailableApiSet;
    }


    /**
     * Stop monitoring Camera events and close liveview connection.
     */
    @Override
    public void closeConnection() {

        Log.d(TAG, "closeConnection(): exec.");
        // Liveview stop
        Log.d(TAG, "closeConnection(): LiveviewSurface.stop()");
        stopLiveview();

        // getEvent stop
        Log.d(TAG, "closeConnection(): EventObserver.release()");
        mEventObserver.release();

        Log.d(TAG, "closeConnection(): completed.");
        apisReady = false;
    }


    @Override
    public void registerResultCallback(ResultCallback resultCallback){
        resultCallbacks.add(resultCallback);
    }

    @Override
    public void registerStateChangeCallback(CameraStateChangeCallback stateChangeCallback) {
        stateChangeCallbacks.add(stateChangeCallback);
    }

    @Override
    public void takeSinglePhoto() {
        JSONObject result = null;
        try {
            result = mRemoteApi.actTakePicture();
            timeOfPictureTaken = System.currentTimeMillis();
            sendResult(result, BracketCameraControllerAPI.SINGLE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void takeSinglePhoto(int shutterspeed) throws IOException {
        int timeout = shutterspeed;
        JSONObject result = null;
        timeout = (int) Math.ceil(shutterspeed * 3.5);
        this.timeout = timeout;
        timeOfPictureTaken = System.currentTimeMillis();
        if(timeout < 15000){
            timeout = 15000;
        }
        Log.i(TAG, "take single photo:  " + timeout);
        if(shutterspeed/1000 > 30){
            Log.i(TAG, "Taking bulb: " + globalShutterSpeed);
            takeBulbPhoto(timeout, shutterspeed);
        }else{
            Log.i(TAG, "Taking single photo: " + globalShutterSpeed);
            result = mRemoteApi.actTakePicture(timeout);
            sendResult(result, BracketCameraControllerAPI.SINGLE_PHOTO);
        }
    }

    /**
     * This tells the camerea to wait for a long exposure.
     * @param timeout
     * @throws IOException
     */
    public void awaitTakePicture(int timeout)throws IOException{
        Bundle timeoutBundle = new Bundle();
        timeoutBundle.putInt(BracketCameraControllerAPI.AWAIT_TAKE_PICTURE, timeout);
        Message message = awaitHandler.obtainMessage(200);
        message.setData(timeoutBundle);
        awaitHandler.sendMessageDelayed(message, 0);
    }

    private void takeBulbPhoto(int timeout, int shutterspeed) throws IOException {
        JSONObject result = null;
        result = mRemoteApi.startBulbShooting(timeout);
        sendResult(result, BracketCameraControllerAPI.SINGLE_PHOTO);

        Bundle timeoutBundle = new Bundle();
        timeoutBundle.putInt(BracketCameraControllerAPI.TIMEOUT, timeout);
        Message message = bulbHandler.obtainMessage(200);
        message.setData(timeoutBundle);
        bulbHandler.sendMessageDelayed(message, shutterspeed);

    }

    private int convertCameraState(String cameraSate) {
        int intCameraState = 0;
        switch(cameraSate){
            case "Error":
                intCameraState = BracketCameraControllerAPI.ERROR;
                break;
            case "NotReady":
                intCameraState = BracketCameraControllerAPI.NOT_READY;
                break;
            case "IDLE":
                intCameraState = BracketCameraControllerAPI.IDLE;
                break;
            case "StillCapturing":
                intCameraState = BracketCameraControllerAPI.STILL_CAPTURING;
                break;
            case "StillSaving":
                intCameraState = BracketCameraControllerAPI.STILL_SAVING;
                break;
            case "MovieWaitRecStart":
                intCameraState = BracketCameraControllerAPI.MOVIE_WAIT_REC_START;
                break;
            case "MovieRecording":
                intCameraState = BracketCameraControllerAPI.MOVIE_RECORDING;
                break;
            case "MovieWaitRecStop":
                intCameraState = BracketCameraControllerAPI.MOVIE_WAIT_REC_STOP;
                break;
            case "MovieSaving":
                intCameraState = BracketCameraControllerAPI.MOVIE_SAVING;
                break;
            case "AudioWaitRecStart":
                intCameraState = BracketCameraControllerAPI.AUDIO_WAIT_REC_START;
                break;
            case "AudioRecording":
                intCameraState = BracketCameraControllerAPI.AUDIO_RECORDING;
                break;
            case "AudioWaitRecStop":
                intCameraState = BracketCameraControllerAPI.AUDIO_WAIT_REC_STOP;
                break;
            case "AudioSaving":
                intCameraState = BracketCameraControllerAPI.AUDIO_SAVING;
                break;
            case "IntervalWaitRecStart":
                intCameraState = BracketCameraControllerAPI.INTERVAL_WAIT_REC_START;
                break;
            case "IntervalRecording":
                intCameraState = BracketCameraControllerAPI.INTERVAL_RECORDING;
                break;
            case "IntervalWaitRecStop":
                intCameraState = BracketCameraControllerAPI.INTERVAL_WAIT_REC_STOP;
                break;
            case "LoopWaitRecStart":
                intCameraState = BracketCameraControllerAPI.LOOP_WAIT_REC_START;
                break;
            case "LoopRecording":
                intCameraState = BracketCameraControllerAPI.LOOP_RECORDING;
                break;
            case "LoopWaitRecStop":
                intCameraState = BracketCameraControllerAPI.LOOP_WAIT_REC_STOP;
                break;
            case "LoopSaving":
                intCameraState = BracketCameraControllerAPI.LOOP_SAVING;
                break;
            case "WhiteBalanceOnePushCapturing":
                intCameraState = BracketCameraControllerAPI.WHITE_BALANCE_ONE_PUSH_CAPTURING;
                break;
            case "ContentsTransfer":
                intCameraState = BracketCameraControllerAPI.CONTENTS_TRANSFER;
                break;
            case "Streaming":
                intCameraState = BracketCameraControllerAPI.STREAMING;
                break;
            case "Deleting":
                intCameraState = BracketCameraControllerAPI.DELETING;
                break;

        }

        return intCameraState;
    }
}
