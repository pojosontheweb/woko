package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet

@FacetKey(name='utf8test', profileId='all')
class Utf8TestFacet extends BaseForwardResolutionFacet {

    @Override
    String getPath() {
        '/WEB-INF/jsp/utf8-message-test.jsp'
    }

    String getWokoMessage() {
        woko.getLocalizedMessage(request, 'utf8.message.test')
    }
}
