package woko.facets.builtin

import woko.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderPropertyName extends IFacet, FragmentFacet {

  static String name = 'renderPropertyName'  

  void setPropertyName(String name)

  String getPropertyName()
}