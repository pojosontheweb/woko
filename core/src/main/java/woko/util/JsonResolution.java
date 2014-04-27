package woko.util;

import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonResolution extends StreamingResolution {

    private static final String JSON_CONTENT_TYPE = "text/json";

    public JsonResolution(JSONObject object) {
        super(JSON_CONTENT_TYPE, object.toString());
    }

    public JsonResolution(JSONArray array) {
        super(JSON_CONTENT_TYPE, array.toString());
    }
}
