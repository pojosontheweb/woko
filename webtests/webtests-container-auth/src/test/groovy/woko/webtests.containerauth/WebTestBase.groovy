package woko.webtests.containerauth

import woko.webtests.WokoWebTestBase

abstract class WebTestBase extends WokoWebTestBase {

  void checkSearchForm(String url){
    goToPage url

    // Check search input is present
    ant.verifyXPath xpath:"/html/body/div/div[1]/div[3]/form[@action='/woko-webtests/search']"
    ant.verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@type='text']"
    ant.verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@name='facet.query']"
    ant.verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@type='submit']"
    ant.verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@name='search']"
  }

}
