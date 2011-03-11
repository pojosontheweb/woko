package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderObject;

@FacetKey(name="renderObject", profileId="all")
public class RenderObjectImpl extends BaseFragmentFacet implements RenderObject {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderObject.jsp";
  }

}
