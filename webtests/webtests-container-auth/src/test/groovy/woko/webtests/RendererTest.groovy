package woko.webtests

class RendererTest extends WebTestBase {

  void testPropertyValueFacetOverrides() {
    webtest('test prop value overrides') {
      login()
      // create test object
      goToPage '/save/MyBook?object._id=1111&object.name=Moby&object.nbPages=123'

      // view
      goToPage '/view/MyBook/1111'
      verifyText '123 page(s)' // view override

      // edit
      goToPage '/edit/MyBook/1111'
      verifyText 'page(s)'

      goToPage '/delete/MyBook/1111?facet.confirm=true'
    }
  }



}
