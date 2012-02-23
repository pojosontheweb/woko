package woko.webtests

class ExceptionTest extends WebTestBase {



    void testException500() {
        webtest("testException500") {
            config {
                option name:"ThrowExceptionOnFailingStatusCode", value:false
            }
            goToPage "/throw"
            storeResponseCode property:"status"
            verifyProperty name:"status", text:"500"
            verifyText 'An error occured'
        }
    }

    void testException404() {
        webtest("testException404") {
            config {
                option name:"ThrowExceptionOnFailingStatusCode", value:false
            }
            goToPage "/dddbbb"
            storeResponseCode property:"status"
            verifyProperty name:"status", text:"404"
            verifyText 'Page not found'
        }
    }


}
