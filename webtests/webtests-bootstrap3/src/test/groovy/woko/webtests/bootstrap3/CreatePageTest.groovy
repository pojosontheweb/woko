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

class CreatePageTest  extends WebTestBase{

    void testCreatePage(){
        webtest('test Create page'){
            login()

            clickLink label:'create'

            verifyText 'Create a new object'

            verifyXPath xpath: "//form[@action='/woko-webtests/save']/select[@name='className']"

            verifyXPath xpath: "//form[@action='/woko-webtests/save']/select[@name='className']/option[@value='MyBook']"
            verifyXPath xpath: "//form[@action='/woko-webtests/save']/input[@type='submit']"
            verifyXPath xpath: "//form[@action='/woko-webtests/save']/input[@name='create']"
        }
    }
}