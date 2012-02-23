package woko.webtests.localization

/**
 * Test all links provide by the different navbar (Guest and devel).<br/>
 * We don't test here the linked actions. They are tested in other web test module.
 */
class NavBarTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test navbar") {

      // Test for guest
      goToPage '/home'

      verifyText 'home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/home']"

      // Test for devel
      login()
      goToPage '/home'

      verifyText 'home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/home']"
      verifyText 'find'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/find']"
      verifyText 'create'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/create']"
      verifyText 'woko studio'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/studio']"



    }
  }
}
