package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.facets.builtin.RenderPropertyValueEdit;

import java.util.Date;

@FacetKeyList(
  keys={
    @FacetKey(name="renderPropertyValueEdit", profileId="all", targetObjectType=String.class),
    @FacetKey(name="renderPropertyValueEdit", profileId="all", targetObjectType=Number.class)
  })
public class RenderPropertyValueEditStripesText extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderPropertyValueEditStripesText.jsp";
  }

}
