package woko.webtests.localization

import woko.webtests.WebTestBase

class HomePageTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test home page") {
      goToPage '/home'
      verifyTitle 'Woko - Guest Home'
      verifyText 'This is guest home !'
      verifyText 'You are not authenticated'

    }
  }

}
