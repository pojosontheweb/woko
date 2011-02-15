package woko.facets.hbcompass

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFragmentFacet

@FacetKey(name="termCloud", profileId="developer")
class TermCloud extends BaseFragmentFacet {

  String getPath() {
    return '/WEB-INF/woko/jsp/developer/'
  }


}
