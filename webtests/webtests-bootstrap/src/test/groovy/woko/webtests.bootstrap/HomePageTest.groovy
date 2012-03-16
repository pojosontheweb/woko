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

package woko.webtests.bootstrap

class HomePageTest extends WebTestBase{

  void testHomePage(){
    webtest('test HomePage') {

      // For guest users
      goToPage '/home'
      // TODO verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      // TODO verifyTitle 'Woko - home'
      verifyText 'This is guest home !'

      // For wdevel
      login()
      goToPage '/home'
      // TODO verifyTitle 'Woko - home'

      // Click 'Home" link in navbar
      clickLink href:'/woko-webtests/home'
      // TODO verifyTitle 'Woko - home'
      verifyText 'This is developer home !'

      // Check search input is present
      checkSearchForm('/home')
    }
  }
}

