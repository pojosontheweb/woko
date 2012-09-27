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



package woko.webtests

class UsermanagementTest extends WokoWebTestBase {

    UsermanagementTest() {
        useContainerAuth = false
    }

    void testUserManagement() {
        webtest("testUserManagement") {
            login()
            goToPage '/list/MyUser'
            verifyXPath xpath:'/html/body/div/div[2]/div/div/table/tbody/tr/td[2]/span/span',
                    text:'.*wdevel.*', regex:true
            verifyXPath xpath:'/html/body/div/div[2]/div/div/table/tbody/tr[2]/td[2]/span/span',
                    text:'.*testuser.*', regex:true

            goToPage '/users'
            verifyXPath xpath:'/html/body/div/div[2]/div/div/table/tbody/tr/td[2]/span/span',
                    text:'.*wdevel.*', regex:true
            verifyXPath xpath:'/html/body/div/div[2]/div/div/table/tbody/tr[2]/td[2]/span/span',
                    text:'.*testuser.*', regex:true
        }
    }

    void testRegister() {
        webtest("testRegister") {

            // register a new user

            goToPage "/register"
            verifyText text:"email"
            verifyText text:"username"
            verifyXPath xpath:"/html/body/div/div[2]/div/div/div/div/form/fieldset/div/div/div/input"
            verifyXPath xpath:"/html/body/div/div[2]/div/div/div/div/form/fieldset/div[2]/div/div/input"
            verifyXPath xpath:"/html/body/div/div[2]/div/div/div/div/form/fieldset/div[3]/div/div/input"
            verifyXPath xpath:"/html/body/div/div[2]/div/div/div/div/form/fieldset/div[4]/div/div/input"

            setInputField name:'facet.username', value:'funkystuff'
            setInputField name:'facet.email', value:'funky@stuff.com'
            setInputField name:'facet.password1', value:'funkystuff'
            setInputField name:'facet.password2', value:'funkystuff'
            setInputField name:'facet.user.prop1', value:'funkystuff'
            clickButton name:'doRegister'

            verifyText text: 'Account not yet active'
            verifyText text:'funky@stuff.com'
            verifyText text:'Welcome funkystuff ! Whats next ?'

            // check that user account exists using developer role
            login()
            goToPage '/users/MyUser?facet.page=101&facet.resultsPerPage=10'
            verifyText text:'funkystuff'

            // edit user
            clickLink xpath:'/html/body/div/div[2]/div/div/table/tbody/tr[2]/td[6]/div/a[2]'
            setSelectField xpath:'/html/body/div/div[2]/div/div/div/div[2]/div/form/fieldset/div/div/select', value:'Active'
            setInputField xpath:'/html/body/div/div[2]/div/div/div/div[2]/div/form/fieldset/div[4]/div/input', value:'developer'
            setSelectField name:'object.accountStatus', value:'Active'
            clickButton name: 'save'

            // logout and try to authenticate with new user
            logout()
            login("funkystuff", "funkystuff")
            verifyText text:'Developer Home'
        }

    }

    void testChangePassword() {
        webtest("testUserManagement") {
            not {
                goToPage "/password"
            }
            login()
            goToPage "/password"

            clickButton xpath: '/html/body/div/div[2]/div/div/div/div/form/fieldset/div[4]/input'
            verifyText text: "Current Password is a required field"
            verifyText text: "New Password is a required field"
            verifyText text: "New Password Confirm is a required field"

            setInputField name:"facet.currentPassword", value: "wdevel"
            setInputField name:"facet.newPassword", value: "foobarbaz"
            setInputField name:"facet.newPasswordConfirm", value: "foobarbaz"
            clickButton xpath: '/html/body/div/div[2]/div/div/div[2]/div/form/fieldset/div[4]/input'
            verifyText text: "Password changed"
        }
    }

}
