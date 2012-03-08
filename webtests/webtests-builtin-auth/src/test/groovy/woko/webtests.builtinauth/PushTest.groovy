package woko.webtests.builtinauth

class PushTest extends WebTestBase {

    void testPush() {
        webtest("testPush") {
            login()
            goToPage '/my'
            verifyText 'hey there'

            goToPage """/push?facet.sources[0]=package woko.test.builtinauth

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name = "my", profileId = "all")
class MyFacet extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        return new StreamingResolution("text/plain", "keep pushin")
    }


}"""
            goToPage '/my'
            verifyText 'keep pushin'


        }
    }

}
