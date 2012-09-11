package woko.facets.builtin.all

import com.kestuboof.facets.builtin.RenderListItem
import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.IFacetContext
import com.sun.xml.internal.rngom.parse.host.Base
import woko.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="renderListItem", profileId="all")
class RenderListItemImpl extends BaseFragmentFacet implements RenderListItem {

    private Object owningObject;

    @Override
    String getPath() {
        return '/WEB-INF/woko/jsp/all/renderListItem.jsp'
    }
}
