package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@FacetKeyList(
keys={
  @FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=Number.class),
  @FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=String.class),
  @FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=Boolean.class)
})
public class RenderPropertyValueJsonBasicTypes extends BaseFacet implements RenderPropertyValueJson {

  public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    // catch-all : return the target object itself
    return propertyValue;
  }


}
