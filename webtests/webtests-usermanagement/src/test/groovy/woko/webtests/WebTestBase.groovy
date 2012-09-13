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



package woko.webtests

abstract class WebTestBase extends WokoWebTestBase {

  def homeUrl = 'http://localhost:9999/woko-webtests'

  void goToPage(String url) {
    ant.invoke(homeUrl + url)
  }

  void login() {
    goToPage '/login'
    ant.setInputField name:'username', value:'wdevel'
    ant.setInputField name:'password', value:'wdevel'
    ant.clickButton name:'login'
    ant.verifyText 'You have been logged in'
  }

  void logout() {
    goToPage '/logout'
  }

}
