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

package woko.webtests.bootstrap

class FindPageTest extends WebTestBase{

    void testFindPage(){
        webtest('test Find page'){
            // Login & click find link in navbar
            login()
            clickLink label:'find'

            // TODO verifyTitle 'Woko - Find objects'
            verifyText 'Full text search'
            verifyText 'Enter your query and submit'
            verifyXPath xpath:"/html/body/div/div[2]/div/form[@action='/woko-webtests/search']"
            verifyXPath xpath:"/html/body/div/div[2]/div/form/input[1][@type='text']"
            verifyXPath xpath:"/html/body/div/div[2]/div/form/input[1][@name='facet.query']"
            verifyXPath xpath:"/html/body/div/div[2]/div/form/input[2][@type='submit']"
            verifyXPath xpath:"/html/body/div/div[2]/div/form/input[2][@name='search']"

            verifyText 'Find objects by class'
            verifyText 'Select the class to list instances for'
            verifyXPath xpath:"/html/body/div/div[2]/div/ul/li[4]/a[@href='/woko-webtests/list/MyBook']"

            // Check search input is present
            checkSearchForm('/find')
        }
    }
}

