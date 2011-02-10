package woko.webtests

class CreatePageTest  extends WebTestBase{

  void testFindPage(){
    webtest('test Create page'){
      login()

      clickLink label:'create'

      verifyTitle 'Woko - Create object'
      verifyText 'Create a new object'
      verifyText 'Select the the class of the object to create, and submit :'

      verifyXPath xpath: "/html/body/div/div[3]/div[2]/form[@action='/woko-webtests/save']"
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/form/select[@name='className']"
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/form/select/option[@value='MyBook']"
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/form/input[@type='submit']"
      verifyXPath xpath: "/html/body/div/div[3]/div[2]/form/input[@name='create']"

      // Check search input is present
      checkSearchForm('/create')

      // Check actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li/a[@href='#']"
    }
  }
}