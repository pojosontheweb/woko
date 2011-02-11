package woko.webtests

class WokoStudioPageTest extends WebTestBase{

  void testFindPage(){
    webtest('test Woko Studio page'){
      login()

      clickLink label:'woko studio'

      verifyTitle 'Woko - Studio'
      verifyText 'Woko Studio'

      // Check search input is present
      checkSearchForm('/studio')

      // Check actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li/a[@href='#']"
    }
  }
}