package woko.facets.builtin

import woko.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderTitle extends IFacet, FragmentFacet {

  static String name = 'renderTitle'

  String getTitle()

}