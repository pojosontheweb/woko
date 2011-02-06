package woko.facets.builtin

import woko.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderProperties extends IFacet, FragmentFacet {

  static String name = 'renderProperties'  

  List<String> getPropertyNames()

  Map<String,Object> getPropertyValues()

}