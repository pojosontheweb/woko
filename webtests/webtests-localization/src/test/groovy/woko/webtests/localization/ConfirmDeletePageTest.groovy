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
 * Test the UI of the confirmDelete jsp.<br/>
 * Feature only available for devel user
 */
class ConfirmDeletePageTest extends WebTestBase {

  void testConfirmDelete() {
    webtest("test confimDelete") {
      login()

      // Create an object first
      goToPage '/save/MyBook?createTransient=true&object._id=1&object.name=Moby'

      // Got to ConfirmDelete
      goToPage '/delete/MyBook/1'

      verifyTitle 'Woko - Moby'
      verifyText 'Please confirm deletion'
      verifyText 'You are about to permanently delete object Moby. Are you sure ?'

      verifyXPath xpath:"/html/body/div/div[3]/div/form[@action='/woko-webtests/delete/MyBook/1']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@value='Delete']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@name='facet.confirm']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@value='Cancel']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@name='facet.cancel']"

      // Delete the object to avoid influencing other tests
      clickButton name:'facet.confirm'
      
    }
  }
}
