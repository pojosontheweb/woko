package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.Layout
import woko2.facets.builtin.all.LayoutAll

@FacetKey(name='layout', profileId='developer')
class LayoutDeveloper extends LayoutAll implements Layout {

  List<String> getCssIncludes() {
    return ['/woko/css/layout-all.css', '/woko/css/lithium/assets/style.css']
  }

}
