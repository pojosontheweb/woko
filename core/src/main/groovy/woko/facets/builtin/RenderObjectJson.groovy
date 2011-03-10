package woko.facets.builtin

import org.json.JSONObject
import net.sourceforge.jfacets.IFacet
import javax.servlet.http.HttpServletRequest

interface RenderObjectJson extends IFacet {

  JSONObject objectToJson(HttpServletRequest request)

}
