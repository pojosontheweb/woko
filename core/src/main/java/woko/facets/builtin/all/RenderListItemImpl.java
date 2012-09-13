package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderListItem;
import woko.facets.builtin.WokoFacets;

@FacetKey(name=WokoFacets.renderListItem, profileId="all")
public class RenderListItemImpl extends BaseFragmentFacet implements RenderListItem {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/jsp/all/renderListItem.jsp";
    }

    @Override
    public String getItemWrapperCssClass() {
        return null;
    }
}
