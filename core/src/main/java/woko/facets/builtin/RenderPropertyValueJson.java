package woko.facets.builtin;

import javax.servlet.http.HttpServletRequest;

public interface RenderPropertyValueJson {

  Object propertyToJson(HttpServletRequest request, Object propertyValue);

}