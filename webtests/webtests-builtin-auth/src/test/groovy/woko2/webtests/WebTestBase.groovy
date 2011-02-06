package woko2.webtests

import com.canoo.webtest.WebtestCase

abstract class WebTestBase extends WebtestCase {

  def homeUrl = 'http://localhost:8080/woko-webtests'

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

}
