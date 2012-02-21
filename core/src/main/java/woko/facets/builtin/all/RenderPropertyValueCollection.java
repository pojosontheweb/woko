package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.WokoFacets;

import java.util.Collection;

@FacetKey(name= WokoFacets.renderPropertyValue, profileId="all", targetObjectType=Collection.class)
public class RenderPropertyValueCollection extends RenderPropertyValueImpl {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertyValueCollection.jsp";
  }


}
