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
 * @author Alexis Boissonnat - alexis.boissonnat 'at' gmail.com
 */
class SavePageTest extends WebTestBase{

    void testSavePage(){
        webtest('test Save page'){
            login()

            goToPage '/save/MyJavaEntity'

            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/span", text:'Please fix the following errors:'

            // Check if the 'cannot be null' hibernate error has been transformed in 'FieldName is a required field' Stripes error
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[1]", text:'The minimum allowed value for Min is 2'
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[2]", text:'Name is a required field'
            verifyXPath xpath: "/html/body/div/div[3]/div/div[1]/ul/li[3]", text:'Range must be between 2 and 10'
        }
    }
}
