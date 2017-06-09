package com.keysersoze.sonyandroidlib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 5/29/17.
 */

public class SonyCameraControllerUtil {

    private static final String TAG = SonyCameraControllerUtil.class.getSimpleName();

    public static String parseCameraStatus(JSONObject eventObject){
        String cameraStatus = "UNKNOWN";

        JSONArray arr = null;
        try {
            arr = eventObject.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < arr.length(); i++)
        {
            try {
                cameraStatus = arr.getJSONObject(i).getString("cameraStatus");
                Log.i(TAG, "camereaStatus Found: " + cameraStatus);
                break;
            } catch (JSONException e) {
                Log.i(TAG, "camereaStatus Not found Checking next array entry " + Integer.toString(i));
            }

            //resultJson.getJSONArray("result").getJSONObject(1).getString("cameraStatus")
        }

        return cameraStatus;
    }

    public static String parseSingleResult(JSONObject resultObject) {
        String stringResult = null;
        try {
            stringResult = resultObject.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringResult;
    }

}
