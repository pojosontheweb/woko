package woko.webtests

class WokoStudioPageTest extends WebTestBase{

  void testStudioPage(){
    webtest('test Woko Studio page'){
      login()

      clickLink label:'woko studio'

      verifyTitle 'Woko - Studio'
      verifyText 'Woko Studio'

      // Check search input is present
      checkSearchForm('/studio')
    }
  }
}