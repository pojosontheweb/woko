package woko.util;

import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.builtin.RenderObjectJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class JsonResolution extends StreamingResolution {

    private static final String JSON_CONTENT_TYPE = "text/json";
    private String callbackName = null;

    public JsonResolution(JSONObject object) {
        super(JSON_CONTENT_TYPE, object.toString());
    }

    public JsonResolution(JSONArray array) {
        super(JSON_CONTENT_TYPE, array.toString());
    }

    public JsonResolution(Map<String,?> object) {
        this(JSON.toJSONObject(object));
    }

    public JsonResolution(List<?> array) {
        this(JSON.toJSONArray(array));
    }

    public JsonResolution(Object targetObject, HttpServletRequest request) {
        this(toJson(targetObject, request));
    }

    public static JSONObject toJson(Object targetObject, HttpServletRequest request) {
        Util.assertArg("targetObject", targetObject);
        Util.assertArg("request", request);
        Woko<?,?,?,?> woko = Woko.getWoko(request.getSession().getServletContext());
        RenderObjectJson roj = woko.getFacet(RenderObjectJson.FACET_NAME, request, targetObject, targetObject.getClass(), true);
        return roj.objectToJson(request);
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
