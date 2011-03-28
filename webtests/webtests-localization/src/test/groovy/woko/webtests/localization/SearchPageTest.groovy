package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * Test the UI of the search.jsp<br/>
 * First, we create 50 objects to provide the full UI components.
 */
class SearchPageTest extends WebTestBase {

  void testSearchPage() {
    webtest("test Search page") {
      login()

      // Create some books
      goToPage "/createDummyObjects"

      // First step, go to search page without criteria -> return no result
      goToPage '/search'

      verifyTitle 'Woko - Search results'
      verifyText '0 object(s) found'

      // Now fill the search box and test the entire UI
      setInputField name:'facet.query', value:'moby'
      clickButton name:'search'
      
      verifyText '50 object(s) found'
      verifyText 'Showing'
      verifyText 'objects / page'

      // remove the objects
      goToPage "/removeDummyObjects"
    }
  }
}
