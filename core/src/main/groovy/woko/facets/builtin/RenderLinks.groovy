package woko.facets.builtin

import woko.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderLinks extends IFacet, FragmentFacet {

  static String name = 'renderLinks'

  def getLinks()

}