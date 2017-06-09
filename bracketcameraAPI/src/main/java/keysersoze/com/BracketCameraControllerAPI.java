package keysersoze.com;

import org.json.JSONObject;

/**
 * Created by aaron on 5/28/17.
 */

public interface BracketCameraControllerAPI {

    static final int CAMERA_NOT_CONNECTED = 0;
    static final int CAMERA_CONNECTING = 1;
    static final int CAMERA_CONNECTED = 2;

    /**
     * Opens a connection to the camera
     */
    public void openConnection();

    /**
     * Closes the connection to the camera
     */
    public void closeConnection();

    /**
     * Start the Live view
     * @return
     */
    public String startLiveview();

    /**
     * Stop the live view
     * @return
     */
    public void stopLiveview();

    public void setIso(String iso);
    public void setShutterSpeed(String shutterSpeed);
    public void setFstop(String fstop);

    public void getIso();
    public void getShutterSpeed();
    public void getFstop();

    public void getCameraState();

    public void registerResultCallback(ResultCallback resultCallback);

    public interface CameraConnectionHandler {
        void onCameraConnected();

        void onCameraReady();
    }

    public interface ResultCallback {
         void resultCallback(JSONObject resultJson);
    }

}
