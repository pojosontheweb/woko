package woko.webtests

import com.canoo.webtest.WebtestCase

abstract class WebTestBase extends WebtestCase {

  def homeUrl = 'http://localhost:9999/woko-webtests'

  void goToPage(String url) {
    ant.invoke(homeUrl + url)
  }

  void login() {
    goToPage '/login'
    ant.setInputField name:'j_username', value:'wdevel'
    ant.setInputField name:'j_password', value:'wdevel'
    ant.clickButton name:'login'
    ant.verifyText 'You have been logged in'
  }

  void logout() {
    goToPage '/logout'
  }

  void checkSearchForm(String url){
    goToPage url

    // Check search input is present
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form[@action='/woko-webtests/search']"
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form/input[1][@type='text']"
    ant.verifyXPath xpath:"/html/body/div/div/div/div/form/input[1][@name='facet.query']"
  }

}
