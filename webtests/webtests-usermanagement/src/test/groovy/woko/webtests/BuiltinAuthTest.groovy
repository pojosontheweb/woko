package woko.webtests

class BuiltinAuthTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("testAuthenticationWithHome") {
      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is guest home !'
      verifyText 'You are not authenticated'

      // login...
      login()

      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // logout
      logout()

      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is guest home !'
    }
  }


}
