package woko2.facets.builtin

import org.json.JSONObject
import net.sourceforge.jfacets.IFacet
import javax.servlet.http.HttpServletRequest

interface RenderObjectJson extends IFacet {

  static String name = 'renderObjectJson'

  JSONObject objectToJson(HttpServletRequest request)

}