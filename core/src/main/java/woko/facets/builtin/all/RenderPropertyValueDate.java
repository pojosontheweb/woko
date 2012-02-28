package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.WokoFacets;

import java.util.Date;

@FacetKey(name = WokoFacets.renderPropertyValue, profileId = "all", targetObjectType = Date.class)
public class RenderPropertyValueDate extends RenderPropertyValueImpl {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueDate.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }


}
