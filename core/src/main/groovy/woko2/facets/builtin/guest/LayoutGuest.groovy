package woko2.facets.builtin.guest

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.Layout
import woko2.facets.builtin.all.LayoutAll

@FacetKey(name='layout', profileId='guest')
class LayoutGuest extends LayoutAll implements Layout {

  String getAppTitle() {
    return 'Woko - Dev mode'
  }

  List<String> getCssIncludes() {
    return ['/woko/css/layout-all.css', '/woko/css/lithium/assets/style.css']
  }

}
