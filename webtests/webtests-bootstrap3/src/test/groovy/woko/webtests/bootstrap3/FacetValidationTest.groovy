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

package woko.webtests.bootstrap3

import org.junit.Test

class FacetValidationTest extends WebTestBase {

    @Test
    void testFacetValidation() {
        goToPage '/facet-validation-test.jsp'
        verifyText 'facet validation test'

        // submit and check that we have nvalidation errors
        byName('doIt').click()
        verifyText 'My facet prop is a required field'

        // now fill in the required field, and submit
        byName('facet.prop').sendKeys('foobar')
        byName('doIt').click()
        verifyText 'you have entered foobar'
    }
}