package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * Test the UI of the List jsp.
 */
class ListPageTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test List page") {
      login()

      goToPage '/list/MyBook'

      verifyTitle 'Woko - Object list'
      verifyText '0 object(s) found for class MyBook'
      verifyText 'Showing'
      verifyText 'objects / page'

      // Actions
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li/a[@href='#']"
      
    }
  }
}
