package woko.webtests

import com.canoo.webtest.WebtestCase

abstract class WebTestBase extends WebtestCase {

  def homeUrl = 'http://localhost:9999/woko-webtests'

  void goToPage(String url) {
    ant.invoke(homeUrl + url)
  }

  void login() {
    goToPage '/login'
    ant.setInputField name:'username', value:'wdevel'
    ant.setInputField name:'password', value:'wdevel'
    ant.clickButton name:'login'
    ant.verifyText 'You have been logged in'
  }

  void logout() {
    goToPage '/logout'
  }

}
