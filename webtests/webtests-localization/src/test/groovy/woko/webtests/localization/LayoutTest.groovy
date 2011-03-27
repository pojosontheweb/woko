package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * Test the layout interface for all roles (guest and devel)<br/>
 * Test all texts, input fields etc...
 *
 */
class LayoutTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test layout") {

      goToPage '/home'

      // For guest user only
      verifyText 'You are not authenticated -'
      verifyXPath xpath:"/html/body/div/div[1]/div[2]/span/a[@href='/woko-webtests/login']"

      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@name='search']"

      // For developer user only
      login()

      verifyText 'Logged in as'
      verifyXPath xpath:"/html/body/div/div[1]/div[2]/span/a[@href='/woko-webtests/logout']"

      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@name='search']"

    }
  }
}
