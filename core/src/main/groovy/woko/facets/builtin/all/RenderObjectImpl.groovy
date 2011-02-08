package woko.facets.builtin.all

import woko.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.RenderObject

@FacetKey(name='renderObject', profileId='all')
class RenderObjectImpl extends BaseFragmentFacet implements RenderObject {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderObject.jsp'
  }

}
