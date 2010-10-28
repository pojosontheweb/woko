package woko2.facets

import net.sourceforge.jfacets.impl.DefaultFacetContext
import woko2.Woko
import net.sourceforge.jfacets.IProfile
import net.sourceforge.jfacets.FacetDescriptor

class WokoFacetContext extends DefaultFacetContext {

  private Woko woko

  def WokoFacetContext(String facetName, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor, Woko woko) {
    super(facetName, profile, targetObject, facetDescriptor)
    this.woko = woko
  }

  Woko getWoko() {
    return woko
  }

}
