package woko.webtests.builtinauth

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

  void testRedirectOnProtectedUrl() {
    webtest("test redirect on authenticated url") {
      goToPage '/studio'
      verifyText 'Please log-in'
      setInputField name:'username', value:'wdevel'
      setInputField name:'password', value:'wdevel'
      clickButton name:'login'
      verifyText 'You have been logged in'
      verifyText 'The following components are configured'
    }
  }

  void testLoginRequiredKeepsTargetUrl() {
    webtest("test redirect on authenticated url keeps target url") {
      goToPage '/studio?foo=bar'
      verifyText 'Please log-in'
      setInputField name:'username', value:'wdevel'
      setInputField name:'password', value:'wdevel'
      clickButton name:'login'
      verifyText 'You have been logged in'
    }

  }


}
