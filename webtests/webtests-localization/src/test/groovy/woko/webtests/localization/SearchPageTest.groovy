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

/**
 * Test the UI of the search.jsp<br/>
 * First, we create 50 objects to provide the full UI components.
 */
class SearchPageTest extends WebTestBase {

  void testSearchPage() {
    webtest("test Search page") {
      login()

      // Create some books
      goToPage "/createDummyObjects"

      // First step, go to search page without criteria -> return no result
      goToPage '/search'

      verifyTitle 'Woko - Search results'
      verifyText '0 object(s) found'

      // Now fill the search box and test the entire UI
      setInputField name:'facet.query', value:'moby'
      clickButton name:'search'
      
      verifyText '50 object(s) found'
      verifyText 'Showing'
      verifyText 'objects / page'

      // remove the objects
      goToPage "/removeDummyObjects"
    }
  }
}
