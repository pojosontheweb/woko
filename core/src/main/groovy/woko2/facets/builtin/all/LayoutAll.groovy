package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFacet
import woko2.facets.builtin.Layout

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
    return computeJspPathFromFacetDescriptor();
  }


}
