package woko.facets.builtin;

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;
import woko.facets.builtin.all.Link;

import java.util.List;

public interface RenderLinks extends IFacet, FragmentFacet {

  List<Link> getLinks();

}