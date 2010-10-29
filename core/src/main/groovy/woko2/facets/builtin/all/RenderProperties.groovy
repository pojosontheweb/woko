package woko2.facets.builtin.all

import woko2.facets.FragmentFacet
import net.sourceforge.jfacets.IFacet

public interface RenderProperties extends IFacet, FragmentFacet {

  List<String> getPropertyNames()

  Map<String,Object> getPropertyValues()

}