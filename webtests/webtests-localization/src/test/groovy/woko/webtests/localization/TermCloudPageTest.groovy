package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * Test the UI of the Term Cloud page.
 * Test only the UI, the results are tested somewhere else.
 */
class TermCloudPageTest  extends WebTestBase{

  void testFindPage(){
    webtest('test Term Cloud page'){
      login()

      goToPage '/termCloud'

      verifyTitle 'Woko - Term Cloud'
      verifyText 'Compass Term Cloud'
    }
  }
}
