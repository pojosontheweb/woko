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

import org.junit.Test
import org.openqa.selenium.By

class ArgsInListTest extends WebTestBase {

    @Test
    void testListItem() {
        login()
        goToPage '/createDummyObjects'
        try {
            goToPage '/list/MyEntity?facet.resultsPerPage=2' // use 2 res per page otherwise elem is off screen on FF
            verifyText 'Should be displayed only on load'

            findr().elem(By.xpath("/html/body/div[2]/ul/li[3]/a")).click() // click page 2
            verifyText 'Should be displayed on each page except on load !'
        } finally {
            goToPage '/removeDummyObjects'
        }
    }
}
