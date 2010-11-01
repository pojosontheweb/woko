package woko2.facets.builtin

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderTitle extends IFacet, FragmentFacet {

  static String name = 'renderTitle'

  String getTitle()

}