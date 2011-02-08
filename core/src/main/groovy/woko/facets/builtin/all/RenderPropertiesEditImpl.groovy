package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='renderPropertiesEdit', profileId='all')
class RenderPropertiesEditImpl extends RenderPropertiesImpl {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderPropertiesEdit.jsp'
  }

}
