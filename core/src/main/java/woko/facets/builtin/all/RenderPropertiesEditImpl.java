package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="renderPropertiesEdit", profileId="all")
public class RenderPropertiesEditImpl extends RenderPropertiesImpl {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertiesEdit.jsp";
  }

}
