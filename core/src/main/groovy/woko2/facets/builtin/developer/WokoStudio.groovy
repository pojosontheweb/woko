package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.FacetDescriptor

@FacetKey(name='studio', profileId='developer')
class WokoStudio extends BaseForwardResolutionFacet {

  List<FacetDescriptor> getFacetDescriptors() {
    def fdm = facetContext.woko.getJFacets().getFacetRepository().getFacetDescriptorManager()
    FacetDescriptor[] descriptors = fdm.descriptors
    return Arrays.asList(descriptors)
  }

}
