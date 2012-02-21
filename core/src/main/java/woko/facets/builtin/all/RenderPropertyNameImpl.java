package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderPropertyName;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.renderPropertyName, profileId="all")
public class RenderPropertyNameImpl extends BaseFragmentFacet implements RenderPropertyName {

  private String propertyName;

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertyName.jsp";
  }

}
