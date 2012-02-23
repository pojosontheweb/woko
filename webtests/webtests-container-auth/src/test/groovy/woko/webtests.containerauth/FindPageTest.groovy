package woko.webtests.containerauth

/**
 * Created by IntelliJ IDEA.
 * User: Alexis Boissonnat - CSTB
 * Date: Feb 10, 2011
 * Time: 5:06:14 PM
  */
class FindPageTest extends WebTestBase{

  void testFindPage(){
    webtest('test Find page'){
      // Login & click find link in navbar
      login()
      clickLink label:'find'

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

      // Check search input is present
      checkSearchForm('/find')
    }
  }
}

