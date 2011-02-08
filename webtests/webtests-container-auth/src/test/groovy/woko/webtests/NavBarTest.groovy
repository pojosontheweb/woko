package woko.webtests

/**
 * Created by IntelliJ IDEA.
 * User: Alexis Boissonnat - CSTB
 * Date: Feb 8, 2011
 * Time: 2:19:22 PM
  */
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
