package woko.webtests.localization

import woko.webtests.WebTestBase

class HomePageTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test home page") {

      // Test Home page UI for Guest user
      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'Guest Home'
      verifyText 'This is guest home !'
      verifyText 'You are not authenticated'

      // Verify Actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[1]/a[@href='/woko-webtests/login']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[2]/a[@href='http://sourceforge.net/projects/woko']"

      // Test Home page UI for Admin user
      login()

      goToPage '/home'
      verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // Check Actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[1]/a[@href='/woko-webtests/find']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[2]/a[@href='/woko-webtests/create']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[3]/a[@href='/woko-webtests/studio']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[4]/a[@href='/woko-webtests/logout']"

    }
  }

}
