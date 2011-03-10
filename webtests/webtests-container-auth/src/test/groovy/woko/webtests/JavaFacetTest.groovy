package woko.webtests

class JavaFacetTest extends WebTestBase {

  void testJavaFacet() {
    webtest('Java facet') {
      goToPage '/javaFacet'
      verifyText 'ok'
    }
  }

}
