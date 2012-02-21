package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;

import java.util.Date;

@FacetKey(name= WokoFacets.renderPropertyValueEdit, profileId="all", targetObjectType=Date.class)
public class RenderPropertyValueEditDate extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

    public String getPath() {
      return "/WEB-INF/woko/jsp/all/renderPropertyValueEditDate.jsp";
    }


}
