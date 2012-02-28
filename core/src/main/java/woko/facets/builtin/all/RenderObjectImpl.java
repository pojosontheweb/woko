package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderObject;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.renderObject, profileId = "all")
public class RenderObjectImpl extends BaseFragmentFacet implements RenderObject {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderObject.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}
