package com.keysersoze.sonyandroidlib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.text.TextUtils;

/**
 * Created by aaron on 5/29/17.
 */

public class SonyCameraControllerUtil {

    private static final String TAG = SonyCameraControllerUtil.class.getSimpleName();

    public static String parseCameraStatus(JSONObject eventObject){
        String cameraStatus = "UNKNOWN";

        JSONArray arr = null;
        try {
            if(eventObject != null) {
                arr = eventObject.getJSONArray("result");
            }else {
                return cameraStatus;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < arr.length(); i++)
        {
            try {
                cameraStatus = arr.getJSONObject(i).getString("cameraStatus");
                break;
            } catch (JSONException e) {
                Log.i(TAG, "camereaStatus Not found Checking next array entry");
            }

            //resultJson.getJSONArray("result").getJSONObject(1).getString("cameraStatus")
        }

        return cameraStatus;
    }

    public static String parseSingleResult(JSONObject resultObject) {
        String stringResult = null;
        try {
            stringResult = resultObject.getString("result");
        } catch (JSONException resultNotFound) {
            Log.w(TAG, "Result not found, fetching error");
            try {
                stringResult = resultObject.getString("error");
            }catch (JSONException errorNotFound){
                Log.e(TAG, "Error not found: " + errorNotFound);
            }

        }
        return stringResult;
    }

    public static int getNormalizedTimeInSeconds(String shutterSpeed){
        int shutterSpeedInt = 1;
        if (!shutterSpeed.contains("/") && !shutterSpeed.contains(".")) {
            if (shutterSpeed.contains("min")) {
                shutterSpeed = TextUtils.split(shutterSpeed, "min")[0];
                shutterSpeedInt = Integer.parseInt(shutterSpeed) * 60;
            } else  {
                shutterSpeedInt = Integer.parseInt(shutterSpeed);
            }
        }
        return shutterSpeedInt;
    }

}
