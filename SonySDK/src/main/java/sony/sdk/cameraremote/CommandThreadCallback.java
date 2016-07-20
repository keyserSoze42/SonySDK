
package sony.sdk.cameraremote;
import org.json.JSONObject;
/**
 * Created by aaron on 7/19/16.
 */

public interface CommandThreadCallback {
    public void threadFinished(JSONObject result);
}