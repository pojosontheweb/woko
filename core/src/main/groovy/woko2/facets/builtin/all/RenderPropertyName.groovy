package woko2.facets.builtin.all

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderPropertyName extends IFacet, FragmentFacet {

  void setPropertyName(String name)

  String getPropertyName()
}