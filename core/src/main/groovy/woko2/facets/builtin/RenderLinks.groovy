package woko2.facets.builtin

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderLinks extends IFacet, FragmentFacet {

  static String name = 'renderLinks'

  def getLinks()

}