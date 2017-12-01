package com.keysersoze.sonyandroidlib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import netscape.javascript.JSException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
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
public class SonyCameraController implements BracketCameraControllerAPI {

    private boolean connectionStatus = false;
    private String cameraAddress;

    private static final String TAG = "SonyCameraController";

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

    public SonyCameraController(Context context, CameraConnectionHandler connectionHandler) {
        this.context = context;
        this.connectionHandler = connectionHandler;
        resultCallbacks = new ArrayList<>();
        stateChangeCallbacks = new ArrayList<>();
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
                Log.d(TAG, "onCameraStatusChanged() called: " + status);
                currentCamerastate = convertCameraState(status);
                for(CameraStateChangeCallback callback : stateChangeCallbacks) {
                    callback.onCameraStateChange(currentCamerastate);
                }
            }

            @Override
            public void onApiListModified(List<String> apis) {
                Log.d(TAG, "onApiListModified() called");

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
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(result);
        }
    }

    @Override
    public void setShutterSpeed(String shutterSpeed) {
        JSONObject result = null;
        try {
            result = mRemoteApi.setShutterSpeed(shutterSpeed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(result);
        }
    }

    @Override
    public void setFstop(String fstop) {
        JSONObject result = null;
        try {
            result = mRemoteApi.setFNumber(fstop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(result);
        }
    }

    @Override
    public void getIso() {
        JSONObject isoResult = null;
        try {
            isoResult = mRemoteApi.getIsoSpeedRate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(isoResult);
        }
    }

    @Override
    public void getShutterSpeed() {
        JSONObject shutterSpeedResult = null;
        try {
            shutterSpeedResult = mRemoteApi.getShutterSpeed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(shutterSpeedResult);
        }
    }

    @Override
    public void getFstop() {
        JSONObject fstopResult = null;
        try {
            fstopResult = mRemoteApi.getFNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(fstopResult);
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
        int cameraStatus = convertCameraState(stringCameraStatus);

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
     * and showing liveview.
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(result);
        }
    }

    @Override
    public void takeSinglePhoto(int shutterspeed) throws IOException {
        int timeout = shutterspeed;
        JSONObject result = null;
        timeout = (int) Math.ceil(shutterspeed * 3.5);
        Log.i(TAG, "setting timeout to takeSinglePhoto: " + timeout);
        try {
            result = mRemoteApi.actTakePicture(timeout);
        } catch (IOException e) {
            throw e;
        }
        for(ResultCallback callback : resultCallbacks) {
            callback.resultCallback(result);
        }
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
