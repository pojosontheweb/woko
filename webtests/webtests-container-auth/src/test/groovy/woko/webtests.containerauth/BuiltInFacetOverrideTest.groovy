package woko.webtests.containerauth

class BuiltInFacetOverrideTest extends WebTestBase {

    void testOverrideDeveloperNavBar() {
        webtest('testOverrideDeveloperNavBar') {
            login()
            verifyText 'google'
        }
    }

}
