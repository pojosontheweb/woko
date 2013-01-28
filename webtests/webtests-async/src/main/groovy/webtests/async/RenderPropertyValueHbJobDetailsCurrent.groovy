package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.all.RenderPropertyValueImpl

@FacetKey(name = "renderPropertyValue_current", profileId="guest", targetObjectType = MyJobDetails.class)
class RenderPropertyValueHbJobDetailsCurrent extends RenderPropertyValueImpl {

    @Override
    String getPath() {
        "/WEB-INF/propValueCurrent.jsp"
    }
}
