package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;

import java.util.Date;

@FacetKeyList(
  keys = {
    @FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all", targetObjectType = String.class),
    @FacetKey(name = WokoFacets.renderPropertyValueEdit, profileId = "all", targetObjectType = Number.class)
  })
public class RenderPropertyValueEditStripesText extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditStripesText.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}
