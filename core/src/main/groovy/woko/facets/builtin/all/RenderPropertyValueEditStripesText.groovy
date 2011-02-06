package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKeyList
import woko.facets.builtin.RenderPropertyValueEdit

@FacetKeyList(
  keys=[
    @FacetKey(name='renderPropertyValueEdit', profileId='all', targetObjectType=String.class),
    @FacetKey(name='renderPropertyValueEdit', profileId='all', targetObjectType=Date.class),
    @FacetKey(name='renderPropertyValueEdit', profileId='all', targetObjectType=Number.class)
  ])
class RenderPropertyValueEditStripesText extends RenderPropertyValueImpl implements RenderPropertyValueEdit {

  def String getFragmentPath(HttpServletRequest request) {
    super.getFragmentPath(request)
    return '/WEB-INF/woko/jsp/all/renderPropertyValueEditStripesText.jsp'
  }

}
