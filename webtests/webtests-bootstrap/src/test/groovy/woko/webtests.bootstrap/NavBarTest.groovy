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

class NavBarTest extends WebTestBase{

  void testNavBarLinks(){
    webtest('test navbar links') {
      // For guest users
      goToPage '/home'
      // verifyTitle 'Woko - home'
      verifyXPath xpath:"/html/body/div/div/div/div/div/ul/li/a[@href='/woko-webtests/home']"

      // For wdevel
      login()
      goToPage '/home'
      // verifyTitle 'Woko - home'
      verifyXPath xpath:"/html/body/div/div/div/div/div/ul/li/a[@href='/woko-webtests/home']"
      verifyXPath xpath:"/html/body/div/div/div/div/div/ul/li[2]/a[@href='/woko-webtests/find']"
      verifyXPath xpath:"/html/body/div/div/div/div/div/ul/li[3]/a[@href='/woko-webtests/create']"
      verifyXPath xpath:"/html/body/div/div/div/div/div/ul/li[4]/a[@href='/woko-webtests/studio']"
    }
  }

}
