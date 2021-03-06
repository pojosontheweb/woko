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

package woko.webtests.localization

/**
 * Test the UI of the List jsp.
 */
class ListPageTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test List page") {
      login()

      goToPage '/list/MyBook'

      verifyTitle 'Woko - Object list'
      verifyText '0 object(s) found for class MyBook'
      verifyText 'Showing'
      verifyText 'objects / page'

    }
  }
}
