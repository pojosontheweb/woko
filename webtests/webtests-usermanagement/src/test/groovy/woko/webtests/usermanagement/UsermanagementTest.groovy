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


package woko.webtests.usermanagement

import com.google.common.base.Predicate
import com.pojosontheweb.selenium.Findr
import com.pojosontheweb.selenium.formz.Select
import org.junit.Ignore
import org.junit.Test
import org.openqa.selenium.WebElement
import woko.webtests.SeleniumTestBase

class UsermanagementTest extends SeleniumTestBase {

    UsermanagementTest() {
        useContainerAuth = false
    }

    void verifyXPath(String xpath, String expectedText, boolean regex) {
        Findr f = byXpath(xpath)
        if (regex) {
            f.where(new Predicate<WebElement>() {
                @Override
                boolean apply(WebElement input) {
                    String text = input.getText()
                    return text.matches(expectedText)
                }
            })
        } else {
            f.where(Findr.textEquals(expectedText))
        }
        f.eval()
    }

    @Ignore
    @Test
    void testUserManagement() {
        login()
        goToPage '/list/MyUser'

        verifyXPath('/html/body/div[2]/table/tbody/tr[1]/td[2]/p',
                '.*wdevel.*', true)
        verifyXPath('/html/body/div[2]/table/tbody/tr[2]/td[2]/p',
                '.*testuser.*', true)

        goToPage '/users'
        verifyXPath('/html/body/div/div[2]/div/table/tbody/tr/td[2]/span/span',
                '.*wdevel.*', true)
        verifyXPath('/html/body/div/div[2]/div/table/tbody/tr[2]/td[2]/span/span',
                '.*testuser.*', true)
    }

    // see  https://github.com/pojosontheweb/woko/issues/183
    @Test
    @Ignore
    void testRegisterValidation() {
        goToPage "/register"
        byName('doRegister').click()
        verifyText 'Username is a required field'
        byName('facet.username').sendKeys('funkystuff')
        byName('doRegister').click()
        not {
            verifyText 'Username is a required field'
        }
    }

    @Test
    @Ignore
    void testRegister() {
        // register a new user
        goToPage "/register"
        verifyText "email"
        verifyText "username"
        verifyXPath "/html/body/div/div[2]/div/div[3]/div/form/fieldset/div/div/div/input"
        verifyXPath "/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[2]/div/div/input"
        verifyXPath "/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[3]/div/div/input"
        verifyXPath "/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[4]/div/div/input"

        byName('facet.username').sendKeys('funkystuff')
        byName('facet.email').sendKeys('funky@stuff.com')
        byName('facet.password1').sendKeys('funkystuff')
        byName('facet.password2').sendKeys('funkystuff')
        byName('facet.user.prop1').sendKeys('funkystuff')
        byName('doRegister').click()

        verifyText 'Account not yet active'
        verifyText 'funky@stuff.com'
        verifyText "Welcome funkystuff ! What's next ?"

        // check that user account exists using developer role
        login()
        goToPage '/users/MyUser?facet.page=101&facet.resultsPerPage=10'
        verifyText 'funkystuff'

        // edit user
        byXpath('/html/body/div/div[2]/div/table/tbody/tr[2]/td[6]/div/a[2]').click()
        Select.selectByVisibleText(byXpath('/html/body/div/div[2]/div/div[3]/div[2]/form/fieldset/div/div/select'), "Active")
        byXpath('/html/body/div/div[2]/div/div[3]/div[2]/form/fieldset/div[5]/div/input').sendKeys('developer')
        byName('save').click()

        // logout and try to authenticate with new user
        logout()
        login("funkystuff", "funkystuff")
        verifyText 'Developer Home'
    }

    @Test
    @Ignore
    void testChangePassword() {
        not {
            goToPage "/password"
        }
        login()
        goToPage "/password"

        byXpath('/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[4]/input').click()
        verifyText "Current Password is a required field"
        verifyText "New Password is a required field"
        verifyText "New Password Confirm is a required field"

        byName("facet.currentPassword").sendKeys("wdevel")
        byName("facet.newPassword").sendKeys("foobarbaz")
        byName("facet.newPasswordConfirm").sendKeys("foobarbaz")
        byXpath('/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[4]/input').click()
        verifyText "Password changed"

        goToPage "/password"

        byName("facet.currentPassword").sendKeys("foobarbaz")
        byName("facet.newPassword").sendKeys("wdevel")
        byName("facet.newPasswordConfirm").sendKeys("wdevel")
        byXpath('/html/body/div/div[2]/div/div[3]/div/form/fieldset/div[4]/input').click()
        verifyText "Password changed"
    }

}
