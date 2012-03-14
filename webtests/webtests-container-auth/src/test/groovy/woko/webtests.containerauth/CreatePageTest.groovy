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

package woko.webtests.containerauth

class CreatePageTest  extends WebTestBase{

  void testCreatePage(){
    webtest('test Create page'){
      login()

      clickLink label:'create'

      verifyTitle 'Woko - Create object'
      verifyText 'Create a new object'
      verifyText 'Select the the class of the object to create, and submit :'

      verifyXPath xpath: "/html/body/div/div[3]/div/form[@action='/woko-webtests/save']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/select[@name='className']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/select/option[@value='MyBook']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/input[@type='submit']"
      verifyXPath xpath: "/html/body/div/div[3]/div/form/input[@name='create']"

      // Check search input is present
      checkSearchForm('/create')
    }
  }
}