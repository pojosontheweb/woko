package woko.webtests

class ExceptionTest extends WebTestBase {

  void testException() {
    webtest("testException") {
        goToPage "/throw"
        verifyText 'An error occured'
    }
  }


}
