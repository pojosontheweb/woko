package woko.facets.builtin

import woko.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderPropertyName extends IFacet, FragmentFacet {

  void setPropertyName(String name)

  String getPropertyName()
}