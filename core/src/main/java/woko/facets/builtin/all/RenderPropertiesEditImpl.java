package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.renderPropertiesEdit, profileId = "all")
public class RenderPropertiesEditImpl extends RenderPropertiesImpl {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertiesEdit.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}
