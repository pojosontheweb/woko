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

class AuthTest extends WebTestBase {

  void testAuthenticationWithHome() {
    webtest("testAuthenticationWithHome") {
      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is guest home !'
      verifyText 'You are not authenticated'

      // login...
      login()

      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // logout
      logout()

      goToPage '/home'
      verifyTitle 'Woko - home'
      verifyText 'This is guest home !'
    }
  }

  void testAuthenticatedUrls() {
    [
            '/view',
            '/edit',
            '/delete',
            '/save',
            '/json',
            '/find',
            '/list',
            '/search',
            '/studio'
    ].each { u ->
      webtest("test authentication on $u") {
        goToPage u
        verifyTitle 'Woko - Please log-in'
        verifyText 'Please log-in'
      }
    }
  }


}
