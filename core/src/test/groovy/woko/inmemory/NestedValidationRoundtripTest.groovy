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

package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext
import woko.actions.WokoActionBean
import woko.actions.TestActionBean
import woko.facets.builtin.developer.SaveImpl

class NestedValidationRoundtripTest extends InMemRoundtripTestBase {

    // make sure Stripes validation works as expected (not really useful but shows how the test works)
    void testBeanValidation() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip trip = mockRoundtrip(ctx, '/testValidate.action', null)
            def ab = trip.getActionBean(TestActionBean.class)
            def errors = ab.context.validationErrors
            assert errors.size() == 1
            assert errors.keySet().iterator().next() == "myProp"
        }
    }

    void testFacetValidation() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip trip = mockRoundtrip(ctx, '/testMeToo', null)
            WokoActionBean ab = trip.getActionBean(WokoActionBean.class)
            // assert prop has not been bound
            assert ab.facet.myProp == null
            // assert validation error has been added
            def errors = ab.context.validationErrors
            assertEquals('unexpected number of errors', 1, errors.size())
            assertEquals('Unexpected key for error', 'facet.myProp', errors.keySet().iterator().next())
            assertEquals('Unexpected message key for error',
                    'testMeToo.myProp',
                    errors.get(errors.keySet().iterator().next()).get(0).getFieldName())
            assertEquals('Unexpected error message',
                    'Required property is a required field',
                    errors.get(errors.keySet().iterator().next()).get(0).getMessage(Locale.ENGLISH))
        }
    }

    void testObjectValidation() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip trip = mockRoundtrip(ctx, 'testObjectValidate', 'testentity.MyValidatedPojo', '2', null) // no params specified but a @NotNull is there !
            WokoActionBean ab = trip.getActionBean(WokoActionBean.class)
            // assert validation error has been added
            def errors = ab.context.validationErrors
            assertEquals('unexpected number of errors', 1, errors.size())
            assertEquals('Unexpected map key for error', 'object.str', errors.keySet().iterator().next())
            assertEquals('Unexpected field key for error',
                    'testentity.MyValidatedPojo.str',
                    errors.get(errors.keySet().iterator().next()).get(0).getFieldName())
            assertEquals('Unexpected error message',
                    'OooohYeah is a required field',
                    errors.get(errors.keySet().iterator().next()).get(0).getMessage(Locale.ENGLISH))
        }
    }

}
