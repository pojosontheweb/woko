package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.FacetDescriptor

@FacetKey(name='studio', profileId='developer')
class WokoStudio extends BaseForwardResolutionFacet {

  String getPath() {
    return '/WEB-INF/woko/jsp/developer/studio.jsp'
  }

  List<FacetDescriptor> getFacetDescriptors() {
    def fdm = facetContext.woko.getJFacets().getFacetRepository().getFacetDescriptorManager()
    FacetDescriptor[] descriptors = fdm.descriptors
    return Arrays.asList(descriptors)
  }

}
