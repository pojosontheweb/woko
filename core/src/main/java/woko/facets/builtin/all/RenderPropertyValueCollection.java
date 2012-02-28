package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.WokoFacets;

import java.util.Collection;

@FacetKey(name = WokoFacets.renderPropertyValue, profileId = "all", targetObjectType = Collection.class)
public class RenderPropertyValueCollection extends RenderPropertyValueImpl {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueCollection.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }


}
