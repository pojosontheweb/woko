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

import com.pojosontheweb.selenium.Findr
import org.junit.Test
import org.openqa.selenium.By
import woko.webtests.SeleniumTestBase

class RememberMeTest extends SeleniumTestBase {

    @Test
    void testRememberMe() {

        // make sure we're not logged in
        goToPage '/home'
        verifyText 'This is guest home !'

        // log in as wdevel
        login 'wdevel', 'wdevel', true
        goToPage '/home'
        verifyText 'This is developer home !'

        // now invoke a facet that invalidates the
        // session on the server
        goToPage '/invalidateMySession'
        findr().elem(By.id("message"))
            .where(Findr.textEquals("SessionInvalidated"))
            .eval()

        // we should still be logged in as devel
        goToPage '/home'
        verifyText 'This is developer home !'
    }
}

