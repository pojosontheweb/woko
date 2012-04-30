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

/**
 * Created by IntelliJ IDEA.
 * User: Alexis Boissonnat - CSTB
 * Date: Feb 10, 2011
 * Time: 5:06:14 PM
  */
class FindPageTest extends WebTestBase{

  void testFindPage(){
    webtest('test Find page'){
      // Login & click find link in navbar
      login()
      clickLink label:'find'

      verifyTitle 'Woko - Find objects'
      verifyText 'Full text search'
      verifyText 'Enter your query and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div/form[@action='/woko-webtests/search']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[3]/div/form/input[2][@name='search']"

      verifyText 'Find objects by class'
      verifyText 'Select the name of the class and submit :'
      verifyXPath xpath:"/html/body/div/div[3]/div/ul/li/a[@href='/woko-webtests/list/MyBook']"

      // Check search input is present
      checkSearchForm('/find')
    }
  }
}

