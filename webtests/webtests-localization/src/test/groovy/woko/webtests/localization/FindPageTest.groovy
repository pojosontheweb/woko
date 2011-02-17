package woko.webtests.localization

import woko.webtests.WebTestBase

/**
 * Test the UI of the find jsp.
 */
class FindPageTest  extends WebTestBase{

  void testFindPage(){
    webtest('test Find page'){

      login()
      goToPage '/find'

      verifyTitle 'Woko - Find objects'
      verifyText 'Full text search'
      verifyText 'Enter your query and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/form[@action='/woko-webtests/search']"
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/form/input[2][@name='search']"

      verifyText 'Find objects by class'
      verifyText 'Select the name of the class and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div[2]/ul/li/a[@href='/woko-webtests/list/MyBook']"

      // Check actions
      verifyText 'Actions'
      verifyXPath xpath:"/html/body/div/div[3]/div[1]/div/div/ul/li/a[@href='#']"
    }
  }
}

