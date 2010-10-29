package woko2.facets.builtin.all

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderLinks extends IFacet, FragmentFacet {

  def getLinks()

}