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

package woko.webtests.builtinauth

class BuiltinAuthTest extends WebTestBase {

    void testAuthenticationWithHome() {
        webtest("testAuthenticationWithHome") {
            goToPage '/home'
            verifyTitle 'Woko - home'
            verifyText 'This is guest home !'
            verifyText 'You are not authenticated'

            // login...
            login()

            goToPage '/home'
            verifyTitle 'Woko - home'
            verifyText 'This is developer home !'

            // logout
            logout()

            goToPage '/home'
            verifyTitle 'Woko - home'
            verifyText 'This is guest home !'
        }
    }

    void testAuthenticatedUrls() {
        [
                '/view',
                '/edit',
                '/delete',
                '/save',
                '/json',
                '/find',
                '/list',
                '/search',
                '/studio'
        ].each { u ->
            webtest("test authentication on $u") {
                goToPage u
                verifyTitle 'Woko - Please log-in'
                verifyText 'Please log-in'
            }
        }
    }

    void testRedirectOnProtectedUrl() {
        webtest("test redirect on authenticated url") {
            config {
                option name: "ThrowExceptionOnScriptError", value: "false"
            }
            goToPage '/studio'
            verifyText 'Please log-in'
            setInputField name: 'username', value: 'wdevel'
            setInputField name: 'password', value: 'wdevel'
            clickButton name: 'login'
            verifyText 'You have been logged in'
            verifyText 'The following components are configured'
        }
    }

    void testLoginRequiredKeepsTargetUrl() {
        webtest("test redirect on authenticated url keeps target url") {
            config {
                option name: "ThrowExceptionOnScriptError", value: "false"
            }
            goToPage '/studio?foo=bar'
            verifyText 'Please log-in'
            setInputField name: 'username', value: 'wdevel'
            setInputField name: 'password', value: 'wdevel'
            clickButton name: 'login'
            verifyText 'You have been logged in'
            verifyText 'The following components are configured'
        }

    }


}
