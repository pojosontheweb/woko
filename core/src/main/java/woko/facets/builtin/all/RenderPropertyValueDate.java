package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;

import java.util.Date;

@FacetKey(name="renderPropertyValue", profileId="all", targetObjectType=Date.class)
public class RenderPropertyValueDate extends RenderPropertyValueImpl {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertyValueDate.jsp";
  }


}
