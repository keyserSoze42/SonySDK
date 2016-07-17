package com.keysersoze.sonyandroidlib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sony.sdk.cameraremote.ServerDevice;
import sony.sdk.cameraremote.SimpleRemoteApi;
import sony.sdk.cameraremote.utils.DisplayHelper;

/**
 * Created by aaron on 7/9/16.
 */
public class CameraSettingsController {

    private SimpleRemoteApi mRemoteApi;
    private ServerDevice serverDevice;
    private static final String TAG = "SettingsController";
    private JSONObject currentEvent = null;

    public CameraSettingsController(){
        mRemoteApi = SimpleRemoteApi.getInstance();
    }


    public void getEvent(){
        try {
            currentEvent = mRemoteApi.getEvent(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call actZoom
     *
     * @param direction
     * @param movement
     */
    private void actZoom(final String direction, final String movement) {
        new Thread() {

            @Override
            public void run() {
                try {
                    JSONObject replyJson = mRemoteApi.actZoom(direction, movement);
                    JSONArray resultsObj = replyJson.getJSONArray("result");
                    int resultCode = resultsObj.getInt(0);
                    if (resultCode == 0) {
                        // Success, but no refresh UI at the point.
                        Log.v(TAG, "actZoom: success");
                    } else {
                        Log.w(TAG, "actZoom: error: " + resultCode);
/*                        DisplayHelper.toast(getApplicationContext(), //
                                R.string.msg_error_api_calling);*/
                    }
                } catch (IOException e) {
                    Log.w(TAG, "actZoom: IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.w(TAG, "actZoom: JSON format error.");
                }
            }
        }.start();
    }

    /**
     * Call actTakePicture
     *
     */
    private void actTakePicture() {
        new Thread() {

            @Override
            public void run() {
                try {
                    JSONObject replyJson = mRemoteApi.actTakePicture();
                    JSONArray resultsObj = replyJson.getJSONArray("result");
                    JSONArray imageUrlsObj = resultsObj.getJSONArray(0);
                } catch (IOException e) {
                    Log.w(TAG, "actZoom: IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.w(TAG, "actZoom: JSON format error.");
                }
            }
        }.start();
    }

}
