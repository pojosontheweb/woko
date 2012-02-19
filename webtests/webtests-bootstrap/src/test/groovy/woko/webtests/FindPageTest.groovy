package woko.webtests

class FindPageTest extends WebTestBase{

  void testFindPage(){
    webtest('test Find page'){
      // Login & click find link in navbar
      login()
      clickLink label:'find'

      // TODO verifyTitle 'Woko - Find objects'
      verifyText 'Full text search'
      verifyText 'Enter your query and submit :'
      verifyXPath xpath:"/html/body/div/div[2]/div/div/form[@action='/woko-webtests/search']"
      verifyXPath xpath:"/html/body/div/div[2]/div/div/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[2]/div/div/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[2]/div/div/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[2]/div/div/form/input[2][@name='search']"

      verifyText 'Find objects by class'
      verifyText 'Select the name of the class and submit :'
      verifyXPath xpath:"/html/body/div/div[2]/div/div/ul/li/a[@href='/woko-webtests/list/MyBook']"

      // Check search input is present
      checkSearchForm('/find')
    }
  }
}

