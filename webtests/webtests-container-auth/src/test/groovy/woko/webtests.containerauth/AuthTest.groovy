package woko.webtests.containerauth

class AuthTest extends WebTestBase {

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

  void testAuthenticatedUrls() {
    [
            '/view',
            '/edit',
            '/delete',
            '/save',
            '/json',
            '/find',
            '/list',
            '/search',
            '/studio'
    ].each { u ->
      webtest("test authentication on $u") {
        goToPage u
        verifyTitle 'Woko - Please log-in'
        verifyText 'Please log-in'
      }
    }
  }


}
