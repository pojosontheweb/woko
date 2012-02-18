package woko.webtests

class CreatePageTest  extends WebTestBase{

  void testCreatePage(){
    webtest('test Create page'){
      login()

      clickLink label:'create'

      // TODO verifyTitle 'Woko - Create object'
      verifyText 'Create a new object'
      verifyText 'Select the the class of the object to create, and submit :'

      verifyXPath xpath: "/html/body/div/div[2]/div/div/form[@action='/woko-webtests/save']"
      verifyXPath xpath: "/html/body/div/div[2]/div/div/form/select[@name='className']"
      verifyXPath xpath: "/html/body/div/div[2]/div/div/form/select/option[@value='MyBook']"
      verifyXPath xpath: "/html/body/div/div[2]/div/div/form/input[@type='submit']"
      verifyXPath xpath: "/html/body/div/div[2]/div/div/form/input[@name='create']"

      // Check search input is present
      checkSearchForm('/create')
    }
  }
}