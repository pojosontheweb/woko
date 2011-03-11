package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;

import javax.servlet.http.HttpServletRequest;

@FacetKey(name="renderPropertyValueJson", profileId="all", targetObjectType=Class.class)
public class RenderPropertyValueJsonClass extends BaseFacet implements RenderPropertyValueJson {

  public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    return getFacetContext().getWoko().getObjectStore().getClassMapping((Class)propertyValue);
  }


}