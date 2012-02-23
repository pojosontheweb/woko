package woko.webtests.bootstrap

class BuiltInFacetOverrideTest extends WebTestBase {

    void testOverrideDeveloperNavBar() {
        webtest('testOverrideDeveloperNavBar') {
            login()
            verifyText 'google'
        }
    }

}
