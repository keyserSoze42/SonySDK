package keysersoze.com;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by aaron on 5/28/17.
 */

public interface BracketCameraControllerAPI {

    public static final int ERROR                             =  -1;   //Error at the server (ex. high temperature, no memory card)
    public static final int NOT_READY                         =  0;    //The server cannot start recording (ex. during initialization, mode transitioning)
    public static final int IDLE                              =  1;    //Ready to record
    public static final int STILL_CAPTURING                   =  2;    //Capturing still images
    public static final int STILL_SAVING                      =  3;    //Saving still images
    public static final int MOVIE_WAIT_REC_START              =  4;    //Preparing to start recording movie
    public static final int MOVIE_RECORDING                   =  5;    //Recording movie
    public static final int MOVIE_WAIT_REC_STOP               =  6;    //Stopping the movie recording
    public static final int MOVIE_SAVING                      =  7;    //Saving movie
    public static final int AUDIO_WAIT_REC_START              =  8;    //Preparing to start recording audio
    public static final int AUDIO_RECORDING                   =  9;    //Recording audio
    public static final int AUDIO_WAIT_REC_STOP               =  10;   //Stopping the audio recording
    public static final int AUDIO_SAVING                      =  11;   //Saving audio
    public static final int INTERVAL_WAIT_REC_START           =  12;   //Preparing to capture interval still images
    public static final int INTERVAL_RECORDING                =  13;   //Capturing interval still images
    public static final int INTERVAL_WAIT_REC_STOP            =  14;   //Stopping interval still images
    public static final int LOOP_WAIT_REC_START               =  15;   //Preparing to start loop recording
    public static final int LOOP_RECORDING                    =  16;   //Running loop recording
    public static final int LOOP_WAIT_REC_STOP                =  17;   //Stopping loop recording
    public static final int LOOP_SAVING                       =  18;   //Saving loop recording movie
    public static final int WHITE_BALANCE_ONE_PUSH_CAPTURING  =  19;   //Capturing the image for white balance custom setup
    public static final int CONTENTS_TRANSFER                 =  20;   //The status ready to transferring images
    public static final int STREAMING                         =  21;   //Streaming the movie
    public static final int DELETING                          =  22;   //Deleting the content

    public static final String SUPPORTED_ISO_RESULT = "SUPPORTED_ISO";
    public static final String SUPPORTED_FSTOP_RESULT = "SUPPORTED_FSTOP";
    public static final String SUPPORTED_SHUTTER_SPEED_RESULT = "SUPPORTED_SHUTTER_SPEED";
    public static final String ISO_RESULT = "ISO";
    public static final String FSTOP_RESULT = "FSTOP";
    public static final String SHUTTER_SPEED_RESULT = "SHUTTER_SPEED";
    public static final String SET_ISO_RESULT = "SET_ISO";
    public static final String SET_FSTOP_RESULT = "SET_FSTOP";
    public static final String SET_SHUTTER_SPEED_RESULT = "SET_SHUTTER_SPEED";
    public static final String SINGLE_PHOTO = "SINGLE_PHOTO";
    public static final String PHOTO_COMPLETE = "PHOTO_COMPLETE";
    public static final String AWAIT_TAKE_PICTURE = "AWAIT_TAKE_PICTURE";
    public static final String TIMEOUT = "TIMEOUT";

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

    /**
     * set the ISO
     * @param iso
     */
    public void setIso(String iso);
    public void setShutterSpeed(String shutterSpeed);
    public void setFstop(String fstop);

    public void getIso() throws IOException;
    public void getShutterSpeed() throws IOException;
    public void getFstop() throws IOException;

    public void getAvailableIso() throws IOException;
    public void getAvailableShutterSpeed() throws IOException;
    public void getAvailableFstop() throws IOException;

    public int getCameraState();
    public void updateCameraState() throws IOException;

    public void registerResultCallback(ResultCallback resultCallback);
    public void registerStateChangeCallback(CameraStateChangeCallback stateChangeCallback);

    /**
     * Take a single photo with the default timeout for the http client
     */
    public void takeSinglePhoto() throws IOException;

    /**
     * Take a single photo but pass the shutter speed to use to calculate a time out.
     * @param shutterspeed
     */
    public void takeSinglePhoto(int shutterspeed) throws IOException ;

    public interface CameraConnectionHandler {
        void onCameraConnected();

        void onCameraReady();
    }

    public interface ResultCallback {
         void resultCallback(String resultString);
    }

    public interface CameraStateChangeCallback {
        void onCameraStateChange(int stateChange);
    }

}
