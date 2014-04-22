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

class FacetValidationTest extends WebTestBase {

    void testFacetValidation() {
        webtest('testFacetValidation') {
            goToPage '/facet-validation-test.jsp'
            verifyText 'facet validation test'

            // submit and check that we have nvalidation errors
            clickButton(name: 'doIt')
            verifyText 'My facet prop is a required field'

            // now fill in the required field, and submit
            setInputField(name: 'facet.prop', value: 'foobar')
            clickButton(name: 'doIt')
            verifyText 'you have entered foobar'
        }
    }

    void testFacetValidationMultiEventsDontValidate() {
        webtest('testFacetValidationMultiEventsDontValidate') {
            goToPage '/multiEventsWithDontValidate'
            verifyText 'myProp:null'

            goToPage '/multiEventsWithDontValidate?facet.myProp=123'
            verifyText 'myProp:123'

            goToPage '/facet-validation-test.jsp'
            clickButton(name: 'doIt2')
            verifyText 'My Prop is a required field'

            goToPage '/multiEventsWithDontValidate?otherEvent=true&facet.myProp=123'
            verifyText 'myProp:123'
        }
    }


}