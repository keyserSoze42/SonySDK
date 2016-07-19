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

        requestJson.put("method", "getAvailableApiList").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getAvailableApiList", requestJson);

        requestJson.put("method", "getApplicationInfo").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getApplicationInfo", requestJson);

        requestJson.put("method", "getShootMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getShootMode", requestJson);

        requestJson.put("method", "setShootMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("setShootMode", requestJson);

        requestJson.put("method", "getAvailableShootMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getAvailableShootMode", requestJson);

        requestJson.put("method", "getSupportedShootMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getSupportedShootMode", requestJson);

        requestJson.put("method", "startContShooting").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("startContShooting", requestJson);

        requestJson.put("method", "stopContShooting").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("stopContShooting", requestJson);

        requestJson.put("method", "startBulbShooting").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("startBulbShooting", requestJson);

        requestJson.put("method", "stopBulbShooting").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("stopBulbShooting", requestJson);

        requestJson.put("method", "startLiveview").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("startLiveview", requestJson);

        requestJson.put("method", "stopLiveview").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("stopLiveview", requestJson);

        requestJson.put("method", "startRecMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("startRecMode", requestJson);

        requestJson.put("method", "stopRecMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("stopRecMode", requestJson);

        requestJson.put("method", "actHalfPressShutter").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("actHalfPressShutter", requestJson);

        requestJson.put("method", "actTakePicture").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("actTakePicture", requestJson);

        requestJson.put("method", "startMovieRec").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("startMovieRec", requestJson);

        requestJson.put("method", "stopMovieRec").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("stopMovieRec", requestJson);

        requestJson.put("method", "actZoom").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("actZoom", requestJson);

        requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getEvent", requestJson);

        requestJson.put("method", "setCameraFunction").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("setCameraFunction", requestJson);

        requestJson.put("method", "getCameraMethodTypes").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");
        apiMap.put("getMethodTypes", requestJson);

        requestJson.put("method", "getAvcontentMethodTypes").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "avContent");
        apiMap.put("getMethodTypes", requestJson);

        requestJson.put("method", "getSchemeList").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "avContent");;
        apiMap.put("getSchemeList", requestJson);

        requestJson.put("method", "getSourceList").put("params", new JSONArray()).put("version", "1.0").put("id", 1).put("service", "avContent");;
        apiMap.put("getSourceList", requestJson);

        requestJson.put("method", "getContentList").put("params", new JSONArray()).put("version", "1.3").put("id", 1).put("service", "avContent");;
        apiMap.put("getContentList", requestJson);

        requestJson.put("method", "setStreamingContent").put("params", new JSONArray()).put("version", "1.0").put("id", 1).put("service", "avContent");;
        apiMap.put("setStreamingContent", requestJson);

        requestJson.put("method", "startStreaming").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("id", 1).put("service", "avContent");;
        apiMap.put("startStreaming", requestJson);

        requestJson.put("method", "stopStreaming").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "avContent");;
        apiMap.put("stopStreaming", requestJson);

        requestJson.put("method", "startLiveviewWithSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("startLiveviewWithSize", requestJson);

        requestJson.put("method", "getLiveviewSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getLiveviewSize", requestJson);

        requestJson.put("method", "getSupportedLiveviewSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedLiveviewSize", requestJson);

        requestJson.put("method", "getAvailableLiveviewSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableLiveviewSize", requestJson);

        requestJson.put("method", "setTouchAFPosition").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setTouchAFPosition", requestJson);

        requestJson.put("method", "getTouchAFPosition").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getTouchAFPosition", requestJson);

        requestJson.put("method", "cancelTouchAFPosition").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("cancelTouchAFPosition", requestJson);

        requestJson.put("method", "setSelfTimer").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setSelfTimer", requestJson);

        requestJson.put("method", "getSelfTimer").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSelfTimer", requestJson);

        requestJson.put("method", "getSupportedSelfTimer").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedSelfTimer", requestJson);

        requestJson.put("method", "getAvailableSelfTimer").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableSelfTimer", requestJson);

        requestJson.put("method", "setExposureMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setExposureMode", requestJson);

        requestJson.put("method", "getExposureMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getExposureMode", requestJson);

        requestJson.put("method", "getSupportedExposureMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedExposureMode", requestJson);

        requestJson.put("method", "getAvailableExposureMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableExposureMode", requestJson);

        requestJson.put("method", "setFocusMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setFocusMode", requestJson);

        requestJson.put("method", "getFocusMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getFocusMode", requestJson);

        requestJson.put("method", "getSupportedFocusMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedFocusMode", requestJson);

        requestJson.put("method", "getAvailableFocusMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableFocusMode", requestJson);

        requestJson.put("method", "setExposureCompensation").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setExposureCompensation", requestJson);

        requestJson.put("method", "getExposureCompensation").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getExposureCompensation", requestJson);

        requestJson.put("method", "getSupportedExposureCompensation").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedExposureCompensation", requestJson);

        requestJson.put("method", "getAvailableExposureCompensation").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableExposureCompensation", requestJson);

        requestJson.put("method", "setFNumber").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setFNumber", requestJson);

        requestJson.put("method", "getSupportedFNumber").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedFNumber", requestJson);

        requestJson.put("method", "getAvailableFNumber").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableFNumber", requestJson);

        requestJson.put("method", "setShutterSpeed").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setShutterSpeed", requestJson);

        requestJson.put("method", "getShutterSpeed").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getShutterSpeed", requestJson);

        requestJson.put("method", "getSupportedShutterSpeed").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedShutterSpeed", requestJson);

        requestJson.put("method", "getAvailableShutterSpeed").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableShutterSpeed", requestJson);

        requestJson.put("method", "setIsoSpeedRate").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setIsoSpeedRate", requestJson);

        requestJson.put("method", "getIsoSpeedRate").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getIsoSpeedRate", requestJson);

        requestJson.put("method", "getSupportedIsoSpeedRate").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedIsoSpeedRate", requestJson);

        requestJson.put("method", "getAvailableIsoSpeedRate").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableIsoSpeedRate", requestJson);

        requestJson.put("method", "setWhiteBalance").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setWhiteBalance", requestJson);

        requestJson.put("method", "getWhiteBalance").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getWhiteBalance", requestJson);

        requestJson.put("method", "getSupportedWhiteBalance").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedWhiteBalance", requestJson);

        requestJson.put("method", "getAvailableWhiteBalance").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableWhiteBalance", requestJson);

        requestJson.put("method", "setProgramShift").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setProgramShift", requestJson);

        requestJson.put("method", "getSupportedProgramShift").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedProgramShift", requestJson);

        requestJson.put("method", "setFlashMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setFlashMode", requestJson);

        requestJson.put("method", "getFlashMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getFlashMode", requestJson);

        requestJson.put("method", "getSupportedFlashMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedFlashMode", requestJson);

        requestJson.put("method", "getAvailableFlashMode").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailableFlashMode", requestJson);

        requestJson.put("method", "setPostviewImageSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("setPostviewImageSize", requestJson);

        requestJson.put("method", "getPostviewImageSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getPostviewImageSize", requestJson);

        requestJson.put("method", "getSupportedPostviewImageSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getSupportedPostviewImageSize", requestJson);

        requestJson.put("method", "getAvailablePostviewImageSize").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getAvailablePostviewImageSize", requestJson);

        requestJson.put("method", "getVersions").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getVersions", requestJson);

        requestJson.put("method", "getMethodTypes").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getMethodTypes", requestJson);

        requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", 1).put("version", "1.0").put("service", "camera");;
        apiMap.put("getEvent", requestJson);

        requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", 1).put("version", "1.1").put("service", "camera");;
        apiMap.put("getEvent", requestJson);

        requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", 1).put("version", "1.2").put("service", "camera");;
        apiMap.put("getEvent", requestJson);

    return apiMap;

}


}