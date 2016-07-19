package sony.sdk.cameraremote;

import java.util.HashMap;
import org.json.JSONObject;
/**
 * Created by aaron on 7/18/16.
 */
public class ApiMap {
    HashMap<String, JSONObject> apiMap = new HashMap();

JSONObject requestJson = new JSONObject().put("method", "getAvailableApiList").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
JSONObject requestJson = new JSONObject().put("method", "getApplicationInfo").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
JSONObject requestJson = new JSONObject().put("method", "getShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "setShootMode").put("params", new JSONArray().put(shootMode)).put("id", id()).put("version", "1.0");
JSONObject requestJson = new JSONObject().put("method", "getAvailableShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "getSupportedShootMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");
JSONObject requestJson = new JSONObject().put("method", "startContShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "stopContShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "startBulbShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "stopBulbShooting").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "startLiveview").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "stopLiveview").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "startRecMode").put("params", new JSONArray()).put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "stopRecMode").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "actHalfPressShutter").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "actTakePicture").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "startMovieRec").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "stopMovieRec").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");
JSONObject requestJson =
        new JSONObject().put("method", "actZoom") //
                .put("params", new JSONArray().put(direction).put(movement)) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "getEvent") //
                .put("params", new JSONArray().put(longPollingFlag)) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "setCameraFunction") //
                .put("params", new JSONArray().put(cameraFunction)) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson = new JSONObject().put("method", "getMethodTypes").put("params", new JSONArray().put("")) //.put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "getMethodTypes") //
                .put("params", new JSONArray().put("")) //
                .put("id", id()).put("version", "1.0"); //

JSONObject requestJson =
        new JSONObject().put("method", "getSchemeList").put("params", new JSONArray()) //
                .put("id", id()).put("version", "1.0");

JSONObject requestJson =
        new JSONObject().put("method", "getSourceList") //
                .put("params", new JSONArray().put(0, params)) //
                .put("version", "1.0").put("id", id());

JSONObject requestJson =
        new JSONObject().put("method", "getContentList").put("params", params) //
                .put("version", "1.3").put("id", id());

JSONObject requestJson =
        new JSONObject().put("method", "setStreamingContent") //
                .put("params", new JSONArray().put(0, params)) //
                .put("version", "1.0").put("id", id());


}


