package woko.facets.builtin;

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;

public interface RenderPropertyValue extends IFacet, FragmentFacet {

  void setOwningObject(Object o);

  Object getOwningObject();

  void setPropertyName(String name);

  String getPropertyName();

  Object getPropertyValue();

  void setPropertyValue(Object v);

}