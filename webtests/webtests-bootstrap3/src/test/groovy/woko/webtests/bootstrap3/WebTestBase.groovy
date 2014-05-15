/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.webtests.bootstrap3

import com.google.common.base.Predicate
import com.pojosontheweb.selenium.ManagedDriverJunit4TestBase
import org.junit.Assert
import org.openqa.selenium.By
import com.pojosontheweb.selenium.Findr
import org.openqa.selenium.WebElement

abstract class WebTestBase extends ManagedDriverJunit4TestBase {

    def port = System.getProperty("woko.webtests.port", "9999")
    def homeUrl = "http://localhost:$port/woko-webtests"
    def useContainerAuth = true

    void goToPage(String url) {
        webDriver.get(homeUrl + url)
        def src = webDriver.pageSource
        if (src.contains("Page not found") || src.contains("An error occured")) {
            throw new RuntimeException("Url $url responded 404 or 500")
        }
    }

    void login() {
        login("wdevel", "wdevel")
    }

    Findr byName(String name) {
        return findr().elem(By.name(name))
    }

    Findr byXpath(String xpath) {
        return findr().elem(By.xpath(xpath))
    }

    private void findByNameAndSendKeys(String name, CharSequence... keys) {
        findr()
                .elem(By.name(name))
                .sendKeys(keys)
    }

    void login(String username, String password) {
        goToPage '/login'
        if (useContainerAuth) {
            findByNameAndSendKeys("j_username", username)
            findByNameAndSendKeys("j_password", password)
        } else {
            findByNameAndSendKeys("username", username)
            findByNameAndSendKeys("password", password)
        }
        findr().elem(By.name("login")).click()

        findr()
                .elem(By.cssSelector("div.alert"))
                .elem(By.tagName("li"))
                .where(Findr.textEquals("You have been logged in"))
                .eval()
    }

    void logout() {
        goToPage '/logout'
    }

    // compat methods to avoid rewriting tests
    // ---------------------------------------

    void verifyText(String text) {
        findr().elem(By.tagName("body"))
            .where(new Predicate<WebElement>() {
                @Override
                boolean apply(WebElement input) {
                    String str = input.getText()
                    return str!=null && str.contains(text)
                }
            })
            .eval()
    }

    void verifyXPath(String xpath) {
        findr().elem(By.xpath(xpath)).eval()
    }

    void not(Closure c) {
        try {
            c()
            Assert.fail("should have failed")
        } catch(Exception e) {
            // normal !
        }
    }

    void clickLink(String label) {
        findr()
                .elemList(By.tagName("a"))
                .where(Findr.textEquals(label))
                .at(0)
                .click()
    }


}
