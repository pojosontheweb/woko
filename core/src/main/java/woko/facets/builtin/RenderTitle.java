package woko.facets.builtin;

import woko.facets.FragmentFacet;
import net.sourceforge.jfacets.IFacet;

public interface RenderTitle extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderTitle";

  String getTitle();

}