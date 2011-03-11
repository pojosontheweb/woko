package woko.facets.builtin;

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;

import java.util.List;
import java.util.Map;

public interface RenderProperties extends IFacet, FragmentFacet {

  List<String> getPropertyNames();

  Map<String,Object> getPropertyValues();

}