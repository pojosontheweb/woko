package woko.util;

import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

public class JsonResolution extends StreamingResolution {

    private static final String JSON_CONTENT_TYPE = "text/json";
    private String callbackName = null;

    public JsonResolution(JSONObject object) {
        super(JSON_CONTENT_TYPE, object.toString());
    }

    public JsonResolution(JSONArray array) {
        super(JSON_CONTENT_TYPE, array.toString());
    }

    public JsonResolution setCallbackName(String callbackName) {
        this.callbackName = callbackName;
        return this;
    }

    @Override
    protected void stream(HttpServletResponse response) throws Exception {
        if (callbackName!=null) {
            response.getWriter().write(
                "/**/" + callbackName + "("
            );
        }
        super.stream(response);
        if (callbackName!=null) {
            response.getWriter().write(")");
        }
    }
}
