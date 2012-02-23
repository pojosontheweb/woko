package woko.webtests.bootstrap

class FacetValidationTest extends WebTestBase{

  void testFacetValidation(){
    webtest('test facet validation'){
      goToPage '/facet-validation-test.jsp'
      verifyText 'facet validation test'

      // submit and check that we have nvalidation errors
      clickButton(name:'doIt')
      verifyText 'Facet Prop is a required field'

      // now fill in the required field, and submit
      setInputField(name:'facet.prop', value:'foobar')
      clickButton(name:'doIt')
      verifyText 'you have entered foobar'
    }
  }
}