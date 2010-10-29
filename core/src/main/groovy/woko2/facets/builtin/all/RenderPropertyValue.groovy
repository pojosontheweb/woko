package woko2.facets.builtin.all

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderPropertyValue extends IFacet, FragmentFacet {

  void setOwningObject(Object o)

  Object getOwningObject()

  void setPropertyName(String name)

  String getPropertyName()

}