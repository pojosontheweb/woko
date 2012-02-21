package woko.webtests

import com.canoo.webtest.WebtestCaseFixed

abstract class WokoWebTestBase extends WebtestCaseFixed {

    def port = System.getProperty("woko.webtests.port", "9999")
    def homeUrl = "http://localhost:$port/woko-webtests"
    def useContainerAuth = true

    void goToPage(String url) {
        ant.invoke(homeUrl + url)
    }

    void login() {
        goToPage '/login'
        if (useContainerAuth) {
            ant.setInputField name: 'j_username', value: 'wdevel'
            ant.setInputField name: 'j_password', value: 'wdevel'
        } else {
            ant.setInputField name:'username', value:'wdevel'
            ant.setInputField name:'password', value:'wdevel'
        }
        ant.clickButton name: 'login'
        ant.verifyText 'You have been logged in'
    }

    void logout() {
        goToPage '/logout'
    }

}
