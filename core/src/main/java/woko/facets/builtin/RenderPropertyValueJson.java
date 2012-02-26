package woko.facets.builtin;

import javax.servlet.http.HttpServletRequest;

public interface RenderPropertyValueJson {

    static final String FACET_NAME = "renderPropertyValueJson";


  Object propertyToJson(HttpServletRequest request, Object propertyValue);

}