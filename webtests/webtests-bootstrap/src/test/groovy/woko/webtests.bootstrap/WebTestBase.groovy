package woko.webtests.bootstrap

import woko.webtests.WokoWebTestBase

abstract class WebTestBase extends WokoWebTestBase {

  void checkSearchForm(String url){
    goToPage url

    // Check search input is present
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form[@action='/woko-webtests/search']"
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form/input[1][@type='text']"
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form/input[1][@name='facet.query']"
  }

}
