package woko2.facets.builtin

import javax.servlet.http.HttpServletRequest

public interface RenderPropertyValueJson {

  static String name = 'renderPropertyValueJson'

  Object propertyToJson(HttpServletRequest request)

}