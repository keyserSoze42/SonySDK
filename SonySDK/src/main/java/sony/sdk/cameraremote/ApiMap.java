package sony.sdk.cameraremote;

import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

/**
 * Created by aaron on 7/18/16.
 */
public class ApiMap {
    static HashMap<String, JSONObject> apiMap = new HashMap();

    static JSONObject requestJson = new JSONObject();
public static HashMap<String, JSONObject> getApiMap() throws JSONException{
    apiMap.put("getAvailableApiList",
            new JSONObject().put("method", "getAvailableApiList")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getApplicationInfo",
            new JSONObject().put("method", "getApplicationInfo")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getShootMode",
            new JSONObject().put("method", "getShootMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setShootMode",
            new JSONObject().put("method", "setShootMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableShootMode",
            new JSONObject().put("method", "getAvailableShootMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedShootMode",
            new JSONObject().put("method", "getSupportedShootMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("startContShooting",
            new JSONObject().put("method", "startContShooting")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("stopContShooting",
            new JSONObject().put("method", "stopContShooting")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("startBulbShooting",
            new JSONObject().put("method", "startBulbShooting")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("stopBulbShooting",
            new JSONObject().put("method", "stopBulbShooting")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("startLiveview",
            new JSONObject().put("method", "startLiveview")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("stopLiveview",
            new JSONObject().put("method", "stopLiveview")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("startRecMode",
            new JSONObject().put("method", "startRecMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("stopRecMode",
            new JSONObject().put("method", "stopRecMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("actHalfPressShutter",
            new JSONObject().put("method", "actHalfPressShutter")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("actTakePicture",
            new JSONObject().put("method", "actTakePicture")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("startMovieRec",
            new JSONObject().put("method", "startMovieRec")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("stopMovieRec",
            new JSONObject().put("method", "stopMovieRec")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("actZoom",
            new JSONObject().put("method", "actZoom")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getEvent",
            new JSONObject().put("method", "getEvent")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setCameraFunction",
            new JSONObject().put("method", "setCameraFunction")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getMethodTypes",
            new JSONObject().put("method", "getCameraMethodTypes")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getMethodTypes",
            new JSONObject().put("method", "getAvcontentMethodTypes")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "avContent"));

    apiMap.put("getSchemeList",
            new JSONObject().put("method", "getSchemeList")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "avContent"));

    apiMap.put("getSourceList",
            new JSONObject().put("method", "getSourceList")
                    .put("params", new JSONArray())
                    .put("version", "1.0").put("id", 1)
                    .put("service", "avContent"));

    apiMap.put("getContentList",
            new JSONObject().put("method", "getContentList")
                    .put("params", new JSONArray())
                    .put("version", "1.3").put("id", 1)
                    .put("service", "avContent"));

    apiMap.put("setStreamingContent",
            new JSONObject().put("method", "setStreamingContent")
                    .put("params", new JSONArray())
                    .put("version", "1.0").put("id", 1)
                    .put("service", "avContent"));

    apiMap.put("startStreaming",
            new JSONObject().put("method", "startStreaming")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("id", 1).put("service", "avContent"));

    apiMap.put("stopStreaming",
            new JSONObject().put("method", "stopStreaming")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "avContent"));

    apiMap.put("startLiveviewWithSize",
            new JSONObject().put("method", "startLiveviewWithSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getLiveviewSize",
            new JSONObject().put("method", "getLiveviewSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedLiveviewSize",
            new JSONObject().put("method", "getSupportedLiveviewSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableLiveviewSize",
            new JSONObject().put("method", "getAvailableLiveviewSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setTouchAFPosition",
            new JSONObject().put("method", "setTouchAFPosition")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getTouchAFPosition",
            new JSONObject().put("method", "getTouchAFPosition")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("cancelTouchAFPosition",
            new JSONObject().put("method", "cancelTouchAFPosition")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setSelfTimer",
            new JSONObject().put("method", "setSelfTimer")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSelfTimer",
            new JSONObject().put("method", "getSelfTimer")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedSelfTimer",
            new JSONObject().put("method", "getSupportedSelfTimer")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableSelfTimer",
            new JSONObject().put("method", "getAvailableSelfTimer")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setExposureMode",
            new JSONObject().put("method", "setExposureMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getExposureMode",
            new JSONObject().put("method", "getExposureMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedExposureMode",
            new JSONObject().put("method", "getSupportedExposureMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableExposureMode",
            new JSONObject().put("method", "getAvailableExposureMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setFocusMode",
            new JSONObject().put("method", "setFocusMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getFocusMode",
            new JSONObject().put("method", "getFocusMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedFocusMode",
            new JSONObject().put("method", "getSupportedFocusMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableFocusMode",
            new JSONObject().put("method", "getAvailableFocusMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setExposureCompensation",
            new JSONObject().put("method", "setExposureCompensation")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getExposureCompensation",
            new JSONObject().put("method", "getExposureCompensation")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedExposureCompensation",
            new JSONObject().put("method", "getSupportedExposureCompensation")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableExposureCompensation",
            new JSONObject().put("method", "getAvailableExposureCompensation")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setFNumber",
            new JSONObject().put("method", "setFNumber")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedFNumber",
            new JSONObject().put("method", "getSupportedFNumber")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableFNumber",
            new JSONObject().put("method", "getAvailableFNumber")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setShutterSpeed",
            new JSONObject().put("method", "setShutterSpeed")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getShutterSpeed",
            new JSONObject().put("method", "getShutterSpeed")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedShutterSpeed",
            new JSONObject().put("method", "getSupportedShutterSpeed")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableShutterSpeed",
            new JSONObject().put("method", "getAvailableShutterSpeed")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setIsoSpeedRate",
            new JSONObject().put("method", "setIsoSpeedRate")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getIsoSpeedRate",
            new JSONObject().put("method", "getIsoSpeedRate")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedIsoSpeedRate",
            new JSONObject().put("method", "getSupportedIsoSpeedRate")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableIsoSpeedRate",
            new JSONObject().put("method", "getAvailableIsoSpeedRate")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setWhiteBalance",
            new JSONObject().put("method", "setWhiteBalance")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getWhiteBalance",
            new JSONObject().put("method", "getWhiteBalance")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedWhiteBalance",
            new JSONObject().put("method", "getSupportedWhiteBalance")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableWhiteBalance",
            new JSONObject().put("method", "getAvailableWhiteBalance")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setProgramShift",
            new JSONObject().put("method", "setProgramShift")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedProgramShift",
            new JSONObject().put("method", "getSupportedProgramShift")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setFlashMode",
            new JSONObject().put("method", "setFlashMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getFlashMode",
            new JSONObject().put("method", "getFlashMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedFlashMode",
            new JSONObject().put("method", "getSupportedFlashMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailableFlashMode",
            new JSONObject().put("method", "getAvailableFlashMode")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("setPostviewImageSize",
            new JSONObject().put("method", "setPostviewImageSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getPostviewImageSize",
            new JSONObject().put("method", "getPostviewImageSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getSupportedPostviewImageSize",
            new JSONObject().put("method", "getSupportedPostviewImageSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getAvailablePostviewImageSize",
            new JSONObject().put("method", "getAvailablePostviewImageSize")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getVersions",
            new JSONObject().put("method", "getVersions")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getMethodTypes",
            new JSONObject().put("method", "getMethodTypes")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getEvent",
            new JSONObject().put("method", "getEvent")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.0")
                    .put("service", "camera"));

    apiMap.put("getEvent",
            new JSONObject().put("method", "getEvent")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.1")
                    .put("service", "camera"));

    apiMap.put("getEvent",
            new JSONObject().put("method", "getEvent")
                    .put("params", new JSONArray())
                    .put("id", 1).put("version", "1.2")
                    .put("service", "camera"));

    return apiMap;

    }
}