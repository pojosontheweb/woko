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
 * Test the layout interface for all roles (guest and devel)<br/>
 * Test all texts, input fields etc...
 *
 */
class LayoutTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("test layout") {

      goToPage '/home'

      // For guest user only
      verifyText 'You are not authenticated -'
      verifyXPath xpath:"/html/body/div/div[1]/div[2]/span/a[@href='/woko-webtests/login']"

      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@name='search']"

      // For developer user only
      login()

      verifyText 'Logged in as'
      verifyXPath xpath:"/html/body/div/div[1]/div[2]/span/a[@href='/woko-webtests/logout']"

      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@type='text']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[1][@name='facet.query']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@type='submit']"
      verifyXPath xpath:"/html/body/div/div[1]/div[3]/form/input[2][@name='search']"

    }
  }
}
