package woko.facets.builtin

import net.sourceforge.jfacets.IFacet
import woko.facets.FragmentFacet


interface RenderListItem extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderListItem";

    String getItemWrapperCssClass();
}
