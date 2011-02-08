package woko.facets.builtin.all

import woko.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.Home

@FacetKey(name='home', profileId='all')
class HomeImpl extends BaseForwardResolutionFacet implements Home {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/home.jsp'
  }

}
