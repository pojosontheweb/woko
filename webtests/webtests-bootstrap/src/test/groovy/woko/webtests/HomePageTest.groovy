package woko.webtests

class HomePageTest extends WebTestBase{

  void testHomePage(){
    webtest('test HomePage') {

      // For guest users
      goToPage '/home'
      // TODO verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      // TODO verifyTitle 'Woko - home'
      verifyText 'This is guest home !'

      // For wdevel
      login()
      goToPage '/home'
      // TODO verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      // TODO verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // Check search input is present
      checkSearchForm('/home')
    }
  }
}

