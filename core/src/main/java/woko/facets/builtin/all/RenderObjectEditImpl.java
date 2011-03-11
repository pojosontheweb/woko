package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderObject;

@FacetKey(name="renderObjectEdit", profileId="all")
public class RenderObjectEditImpl extends BaseFragmentFacet implements RenderObject {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderObjectEdit.jsp";
  }


}
