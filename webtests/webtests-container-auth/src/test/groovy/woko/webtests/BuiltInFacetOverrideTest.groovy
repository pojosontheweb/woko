package woko.webtests

class BuiltInFacetOverrideTest extends WebTestBase {

    void testOverrideDeveloperNavBar() {
        webtest('testOverrideDeveloperNavBar') {
            login()
            verifyText 'google'
        }
    }

}
