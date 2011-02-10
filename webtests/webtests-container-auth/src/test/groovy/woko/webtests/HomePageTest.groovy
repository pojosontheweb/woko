package woko.webtests

class HomePageTest extends WebTestBase{

  void testHomePage(){
    webtest('test HomePage') {

      // For guest users
      goToPage '/home'
      verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      verifyTitle 'Woko - home'
      verifyText 'This is guest home !'

      // Check search input is present
      checkSearchForm('/home')

      // Check Actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[1]/a[@href='/woko-webtests/login']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[2]/a[@href='http://sourceforge.net/projects/woko']"

      // For wdevel
      login()
      goToPage '/home'
      verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // Check search input is present
      checkSearchForm('/home')

      // Check Actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[1]/a[@href='/woko-webtests/find']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[2]/a[@href='/woko-webtests/create']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[3]/a[@href='/woko-webtests/studio']"
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li[4]/a[@href='/woko-webtests/logout']"

    }
  }
}

