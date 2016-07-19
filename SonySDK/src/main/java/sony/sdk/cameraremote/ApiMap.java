package sony.sdk.cameraremote;

import java.util.HashMap;
import org.json.JSONObject;
/**
 * Created by aaron on 7/18/16.
 */
public class ApiMap {
    HashMap<String, JSONObject> apiMap = new HashMap();

    JSONObject requestJson = new JSONObject();

	requestJson.put("method", "getAvailableApiList").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableApiList", requestJson);
	
	requestJson.put("method", "getApplicationInfo").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getApplicationInfo", requestJson);

	requestJson.put("method", "getShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getShootMode", requestJson);

	requestJson.put("method", "setShootMode").put("params", new JSONArray().put(shootMode)).put("id", id()).put("version", "1.0");
	apiMap.put("setShootMode", requestJson);

	requestJson.put("method", "getAvailableShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableShootMode", requestJson);

	requestJson.put("method", "getSupportedShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedShootMode", requestJson);

	requestJson.put("method", "startContShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("startContShooting", requestJson);

	requestJson.put("method", "stopContShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopContShooting", requestJson);

	requestJson.put("method", "startBulbShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("startBulbShooting", requestJson);

	requestJson.put("method", "stopBulbShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopBulbShooting", requestJson);

	requestJson.put("method", "startLiveview").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("startLiveview", requestJson);

	requestJson.put("method", "stopLiveview").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopLiveview", requestJson);

	requestJson.put("method", "startRecMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("startRecMode", requestJson);

	requestJson.put("method", "stopRecMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopRecMode", requestJson);

	requestJson.put("method", "actHalfPressShutter").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("actHalfPressShutter", requestJson);

	requestJson.put("method", "actTakePicture").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("actTakePicture", requestJson);

	requestJson.put("method", "startMovieRec").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("startMovieRec", requestJson);

	requestJson.put("method", "stopMovieRec").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopMovieRec", requestJson);

	requestJson.put("method", "actZoom").put("params", new JSONArray().put(direction).put(movement)).put("id", id()).put("version", "1.0");
	apiMap.put("actZoom", requestJson);

	requestJson.put("method", "getEvent").put("params", new JSONArray().put(longPollingFlag)).put("id", id()).put("version", "1.0");
	apiMap.put("getEvent", requestJson);

	requestJson.put("method", "setCameraFunction").put("params", new JSONArray().put(cameraFunction)).put("id", id()).put("version", "1.0");
	apiMap.put("setCameraFunction", requestJson);

	requestJson.put("method", "getMethodTypes").put("params", new JSONArray().put("")) //.put("id", id()).put("version", "1.0");
	apiMap.put("getMethodTypes", requestJson);

	requestJson.put("method", "getMethodTypes").put("params", new JSONArray().put("")).put("id", id()).put("version", "1.0");
	apiMap.put("getMethodTypes", requestJson);

	requestJson.put("method", "getSchemeList").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSchemeList", requestJson);

	requestJson.put("method", "getSourceList").put("params", new JSONArray().put(0, params)).put("version", "1.0").put("id", id());
	apiMap.put("getSourceList", requestJson);

	requestJson.put("method", "getContentList").put("params", params).put("version", "1.3").put("id", id());
	apiMap.put("getContentList", requestJson);

	requestJson.put("method", "setStreamingContent").put("params", new JSONArray().put(0, params)).put("version", "1.0").put("id", id());
	apiMap.put("setStreamingContent", requestJson);

	requestJson.put("method", "startStreaming").put("params", new JSONArray()).put("id", id()).put("version", "1.0").put("id", id());
	apiMap.put("startStreaming", requestJson);

	requestJson.put("method", "stopStreaming").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("stopStreaming", requestJson);

	requestJson.put("method", "startLiveviewWithSize").put("params", params).put("id", id()).put("version", "1.0");
	apiMap.put("startLiveviewWithSize", requestJson);

	requestJson.put("method", "getLiveviewSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getLiveviewSize", requestJson);

	requestJson.put("method", "getSupportedLiveviewSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedLiveviewSize", requestJson);

	requestJson.put("method", "getAvailableLiveviewSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableLiveviewSize", requestJson);

	requestJson.put("method", "setTouchAFPosition").put("params", params).put("id", id()).put("version", "1.0");
	apiMap.put("setTouchAFPosition", requestJson);

	requestJson.put("method", "getTouchAFPosition").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getTouchAFPosition", requestJson);

	requestJson.put("method", "cancelTouchAFPosition").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("cancelTouchAFPosition", requestJson);

	requestJson.put("method", "setSelfTimer").put("params", param).put("id", id()).put("version", "1.0");
	apiMap.put("setSelfTimer", requestJson);

	requestJson.put("method", "getSelfTimer").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSelfTimer", requestJson);

	requestJson.put("method", "getSupportedSelfTimer").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedSelfTimer", requestJson);

	requestJson.put("method", "getAvailableSelfTimer").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableSelfTimer", requestJson);

	requestJson.put("method", "setExposureMode").put("params", exposureModeSetting).put("id", id()).put("version", "1.0");
	apiMap.put("setExposureMode", requestJson);

	requestJson.put("method", "getExposureMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getExposureMode", requestJson);

	requestJson.put("method", "getSupportedExposureMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedExposureMode", requestJson);

	requestJson.put("method", "getAvailableExposureMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableExposureMode", requestJson);

	requestJson.put("method", "setFocusMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setFocusMode", requestJson);

	requestJson.put("method", "getFocusMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getFocusMode", requestJson);

	requestJson.put("method", "getSupportedFocusMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedFocusMode", requestJson);

	requestJson.put("method", "getAvailableFocusMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableFocusMode", requestJson);

	requestJson.put("method", "setExposureCompensation").put("params", params).put("id", id()).put("version", "1.0");
	apiMap.put("setExposureCompensation", requestJson);

	requestJson.put("method", "getExposureCompensation").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getExposureCompensation", requestJson);

	requestJson.put("method", "getSupportedExposureCompensation").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedExposureCompensation", requestJson);

	requestJson.put("method", "getAvailableExposureCompensation").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableExposureCompensation", requestJson);

	requestJson.put("method", "setFNumber").put("params", params).put("id", id()).put("version", "1.0");
	apiMap.put("setFNumber", requestJson);

	requestJson.put("method", "getSupportedFNumber").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedFNumber", requestJson);

	requestJson.put("method", "getAvailableFNumber").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableFNumber", requestJson);

	requestJson.put("method", "setShutterSpeed").put("params", params).put("id", id()).put("version", "1.0");
	apiMap.put("setShutterSpeed", requestJson);

	requestJson.put("method", "getShutterSpeed").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getShutterSpeed", requestJson);

	requestJson.put("method", "getSupportedShutterSpeed").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedShutterSpeed", requestJson);

	requestJson.put("method", "getAvailableShutterSpeed").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableShutterSpeed", requestJson);

	requestJson.put("method", "setIsoSpeedRate").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setIsoSpeedRate", requestJson);

	requestJson.put("method", "getIsoSpeedRate").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getIsoSpeedRate", requestJson);

	requestJson.put("method", "getSupportedIsoSpeedRate").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedIsoSpeedRate", requestJson);

	requestJson.put("method", "getAvailableIsoSpeedRate").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableIsoSpeedRate", requestJson);

	requestJson.put("method", "setWhiteBalance").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setWhiteBalance", requestJson);

	requestJson.put("method", "getWhiteBalance").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getWhiteBalance", requestJson);

	requestJson.put("method", "getSupportedWhiteBalance").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedWhiteBalance", requestJson);

	requestJson.put("method", "getAvailableWhiteBalance").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableWhiteBalance", requestJson);

	requestJson.put("method", "setProgramShift").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setProgramShift", requestJson);
	
	requestJson.put("method", "getSupportedProgramShift").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedProgramShift", requestJson);

	requestJson.put("method", "setFlashMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setFlashMode", requestJson);

	requestJson.put("method", "getFlashMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getFlashMode", requestJson);

	requestJson.put("method", "getSupportedFlashMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedFlashMode", requestJson);

	requestJson.put("method", "getAvailableFlashMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailableFlashMode", requestJson);

	requestJson.put("method", "setPostviewImageSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("setPostviewImageSize", requestJson);

	requestJson.put("method", "getPostviewImageSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getPostviewImageSize", requestJson);

	requestJson.put("method", "getSupportedPostviewImageSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getSupportedPostviewImageSize", requestJson);

	requestJson.put("method", "getAvailablePostviewImageSize").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getAvailablePostviewImageSize", requestJson);

	requestJson.put("method", "getVersions").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getVersions", requestJson);

	requestJson.put("method", "getMethodTypes").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getMethodTypes", requestJson);

	requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
	apiMap.put("getEvent", requestJson);

	requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", id()).put("version", "1.1");
	apiMap.put("getEvent", requestJson);

	requestJson.put("method", "getEvent").put("params", new JSONArray()).put("id", id()).put("version", "1.2");
	apiMap.put("getEvent", requestJson);


}