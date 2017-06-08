package com.keysersoze.sonyandroidlib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaron on 5/29/17.
 */

public class SonyCameraControllerUtil {

    public static String parseCameraStatus(JSONObject eventObject){
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
