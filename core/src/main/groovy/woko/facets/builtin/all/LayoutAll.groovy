package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFacet
import woko.facets.builtin.Layout

@FacetKey(name='layout', profileId='all')
class LayoutAll extends BaseFacet implements Layout {

  String getAppTitle() {
    return 'Woko'
  }

  List<String> getCssIncludes() {
    return ['/woko/css/layout-all.css', '/woko/css/lithium/assets/style.css']
  }

  List<String> getJsIncludes() {
    return []
  }

  String getLayoutPath() {
    return '/WEB-INF/woko/jsp/all/layout.jsp'
  }


}
