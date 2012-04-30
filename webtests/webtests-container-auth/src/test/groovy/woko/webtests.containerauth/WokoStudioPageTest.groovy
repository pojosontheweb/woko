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

package woko.webtests.containerauth

import org.junit.runners.JUnit4
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(JUnit4.class)
class WokoStudioPageTest extends WebTestBase {

    @Test
    void testStudioPage() {
        webtest('test Woko Studio page') {

            config {
                option name: "ThrowExceptionOnScriptError", value: "false"
            }
            
            login()

            clickLink label: 'woko studio'

            verifyTitle 'Woko - Studio'
            verifyText 'Woko Studio'

            // Check search input is present
            checkSearchForm('/studio')
        }
    }
}