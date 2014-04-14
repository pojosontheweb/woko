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

class ArgsInListTest extends WebTestBase {

    void testListItem() {
        webtest('Arguments in list') {
            login()

            // create
            goToPage '/createDummyObjects'

            goToPage '/list/MyEntity'
            verifyText 'Should be displayed only on load'

            clickLink xpath: '/html/body/div/div[2]/div/div[4]/ul/li[3]/a' // click page 2
            verifyText 'Should be displayed on each page except on load !'

            // delete
            goToPage '/removeDummyObjects'
        }
    }
}
