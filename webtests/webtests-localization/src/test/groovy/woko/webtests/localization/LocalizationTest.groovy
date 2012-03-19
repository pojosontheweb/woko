/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package woko.webtests.localization

class LocalizationTest extends WebTestBase {

    void testLocalization() {
        webtest("test localization") {

            // Test the localization
            goToPage '/testLocalization'
            verifyTitle 'Woko - LocalizationTitle'
            verifyText : 'This is an important message'
            verifyText 'Guest Home'
            verifyText 'Woko message (woko.guest.home.content) overridden from application resources'
        }
    }

    void testMissingKeysDontGenerateUglyLabels() {
        webtest("testMissingKeysDontGenerateUglyLabels") {
            login()
            goToPage '/save/MyBook'
            verifyText "Number of pages"
            not {
                verifyText "???MyBook.anotherProp???" // the message doesn't exist for the prop, we check if we don't have an ugly ???xyz??? message
            }
        }
    }
}
