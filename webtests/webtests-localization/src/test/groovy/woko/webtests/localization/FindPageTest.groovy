package woko.webtests.localization

/**
 * Test the UI of the find jsp.
 */
class FindPageTest  extends WebTestBase{

  void testFindPage(){
    webtest('test Find page'){

      login()
      goToPage '/find'

      verifyTitle 'Woko - Find objects'
      verifyText 'Full text search'
      verifyText 'Enter your query and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div/form[@action='/woko-webtests/search']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@name='search']"

      verifyText 'Find objects by class'
      verifyText 'Select the name of the class and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div/ul/li/a[@href='/woko-webtests/list/MyBook']"
    }
  }
}

