package woko2.facets

import net.sourceforge.jfacets.IFacetContextFactory
import net.sourceforge.jfacets.IFacetContext
import net.sourceforge.jfacets.IProfile
import net.sourceforge.jfacets.FacetDescriptor
import woko2.Woko

class WokoFacetContextFactory implements IFacetContextFactory {

  private final Woko woko

  def WokoFacetContextFactory(Woko woko) {
    this.woko = woko;
  }

  IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
    return new WokoFacetContext(name, profile, targetObject, facetDescriptor, woko)
  }


}
