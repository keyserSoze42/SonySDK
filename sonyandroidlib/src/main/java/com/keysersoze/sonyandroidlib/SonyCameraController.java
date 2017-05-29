package com.keysersoze.sonyandroidlib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import keysersoze.com.BracketCameraControllerAPI;
import sony.sdk.cameraremote.ServerDevice;
import sony.sdk.cameraremote.SimpleCameraEventObserver;
import sony.sdk.cameraremote.SimpleRemoteApi;
import sony.sdk.cameraremote.SimpleSsdpClient;
import sony.sdk.cameraremote.utils.thread.ThreadPoolHelper;

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
    private static ThreadPoolHelper threadPoolHelper;
    private Context context;
    private SimpleSsdpClient ssdpClient;

    public SonyCameraController(Context context, CameraConnectionHandler connectionHandler) {
        this.context = context;
        this.connectionHandler = connectionHandler;
        threadPoolHelper = ThreadPoolHelper.getInstance();
    }

    /*
     * Interface for SSDP
     */
    public void cameraDeviceFound(ServerDevice serverDevice) {
        final String deviceAddress = serverDevice.getDDUrl();
        cameraAddress = deviceAddress;
        this.serverDevice = serverDevice;

        mEventListener = new SimpleCameraEventObserver.ChangeListenerTmpl() {

            @Override
            public void onShootModeChanged(String shootMode) {
                Log.d(TAG, "onShootModeChanged() called: " + shootMode);
            }

            @Override
            public void onCameraStatusChanged(String status) {
                Log.d(TAG, "onCameraStatusChanged() called: " + status);
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
        try {
            mRemoteApi.setIsoSpeedRate(isoSpeed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setShutterSpeed(String shutterSpeed) {
        try {
            mRemoteApi.setShutterSpeed(shutterSpeed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFstop(String fstop) {
        try {
            mRemoteApi.setFNumber(fstop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIso() {
        String iso = "UNKNOWN";
        try {
            JSONObject isoResult = mRemoteApi.getIsoSpeedRate();
            iso = (String) isoResult.getJSONArray("result").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return iso;
    }

    @Override
    public String getShutterSpeed() {
        String shutterSpeed = "UNKNOWN";
        try {
            JSONObject isoResult = mRemoteApi.getShutterSpeed();
            shutterSpeed = (String) isoResult.getJSONArray("result").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shutterSpeed;
    }

    @Override
    public String getFstop() {
        String fstop = "UNKNOWN";
        try {
            JSONObject isoResult = mRemoteApi.getFNumber();
            fstop = (String) isoResult.getJSONArray("result").get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fstop;

    }

    @Override
    public String getCameraState() {
        JSONArray supportedVersions;
        String latestVersion = "1.0";

        try {
            JSONObject versionResult = mRemoteApi.getVersions();
            supportedVersions = versionResult.getJSONArray("result");
            latestVersion = (String) supportedVersions.get(supportedVersions.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject eventObject = null;
        try {
            switch (latestVersion) {
                case "1.1":
                    eventObject = mRemoteApi.getEventv1_1(false);
                case "1.2":
                    eventObject = mRemoteApi.getEventv1_2(false);
                default:
                    eventObject = mRemoteApi.getEventv1_0(false);
            }
        }catch (IOException e) {

        }

        String cameraStatus = "UNKNOWN";
        try {
            if(eventObject != null){
                JSONArray eventResult = eventObject.getJSONArray("cameraStatus");
                cameraStatus = eventResult.getString(0);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cameraStatus;
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

    public interface CameraConnectionHandler {
        void onCameraConnected();

        void onCameraReady();
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

        Log.d(TAG, "closeConnection(): Shutdown the executor");
        threadPoolHelper.shutdownNow();
        threadPoolHelper = null;

        Log.d(TAG, "closeConnection(): completed.");
    }
}
