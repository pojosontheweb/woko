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
 * Test all links provide by the different navbar (Guest and devel).<br/>
 * We don't test here the linked actions. They are tested in other web test module.
 */
class NavBarTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test navbar") {

      // Test for guest
      goToPage '/home'

      verifyText 'home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/home']"

      // Test for devel
      login()
      goToPage '/home'

      verifyText 'home'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/home']"
      verifyText 'find'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/find']"
      verifyText 'create'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/create']"
      verifyText 'woko studio'
      verifyXPath xpath:"/html/body/div/div[2]/div/ul/li/a[@href='/woko-webtests/studio']"



    }
  }
}
