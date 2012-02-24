package woko.webtests.bootstrap

class CompassCloudTest extends WebTestBase {

    void testCloud() {
        webtest('test Cloud') {
            login()
            // create test objects
            goToPage "/createDummyObjects"

            // assert our cloud page
            goToPage "/termCloud"
            verifyText "Compass Term Cloud"
            verifyText "moby"
        }
    }

}
