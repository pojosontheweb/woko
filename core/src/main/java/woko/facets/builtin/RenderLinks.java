package woko.facets.builtin;

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;

// TODO find better : this is really crappy, we don't have typing etc.
public interface RenderLinks extends IFacet, FragmentFacet {

  Object getLinks();

}