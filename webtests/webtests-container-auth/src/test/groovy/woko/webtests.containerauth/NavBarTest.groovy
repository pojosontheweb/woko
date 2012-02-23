package woko.webtests.containerauth

class NavBarTest extends WebTestBase{

  void testNavBarLinks(){
    webtest('test navbar links') {
      // For guest users
      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/home']"

      // For wdevel
      login()
      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li[1]/a[@href='/woko-webtests/home']"
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li[2]/a[@href='/woko-webtests/find']"
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li[3]/a[@href='/woko-webtests/create']"
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li[4]/a[@href='/woko-webtests/studio']"
      

      
    }
  }

}
