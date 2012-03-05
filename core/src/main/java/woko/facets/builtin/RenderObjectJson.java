package woko.facets.builtin;

import org.json.JSONObject;
import net.sourceforge.jfacets.IFacet;

import javax.servlet.http.HttpServletRequest;

public interface RenderObjectJson extends IFacet {

    static final String FACET_NAME = "renderObjectJson";

    JSONObject objectToJson(HttpServletRequest request);

}
