package woko.webtests.localization

/**
 * Test the UI of the create jsp.
 */
class CreatePageTest extends WebTestBase{

  void testCreatePage(){
    webtest('test Create page'){
      login()

      goToPage '/create'

      verifyTitle 'Woko - Create object'
      verifyText 'Create a new object'
      verifyText 'Select the the class of the object to create, and submit :'

      verifyXPath xpath: "/html/body/div/div[3]/div/form[@action='/woko-webtests/save']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/select[@name='className']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/select/option[@value='MyBook']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/input[@type='submit']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/input[@name='create']"
    }
  }
}