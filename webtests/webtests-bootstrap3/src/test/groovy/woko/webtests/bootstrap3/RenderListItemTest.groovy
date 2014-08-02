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
import woko.webtests.SeleniumTestBase

class RenderListItemTest extends SeleniumTestBase {

    @Test
    void testListItem() {
        login()

        // create
        goToPage '/save/MyBook?createTransient=true&object._id=1&object.name=Moby&nbPages=50'
        verifyText 'Object saved'

        goToPage '/list/MyBook'
        verifyText 'Moby'
        verifyXPath "/html/body/div[2]/div/div[1][@class='row TestCssClass']"

        // delete
        goToPage '/delete/MyBook/1'
        verifyText 'Please confirm deletion'
        byName('facet.confirm').click()
        verifyText 'Object deleted'

        not {
            goToPage '/view/MyBook/1'
        }
    }
}
