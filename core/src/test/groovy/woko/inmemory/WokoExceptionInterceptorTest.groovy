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
import woko.actions.WokoExceptionInterceptor

class WokoExceptionInterceptorTest extends InMemRoundtripTestBase {

    void testExceptionMarker() {
        doWithMockContext("wdevel") { MockServletContext c ->
            MockRoundtrip t = null
            boolean throwing = false
            try {
                t = new MockRoundtrip(c, "/testThrows")
                t.execute();
                fail("should have thrown")
            } catch (Exception e) {
                // normal behavior
                throwing = true
            }
            assert t
            assert throwing
            assert WokoExceptionInterceptor.getException(t.request)
        }
    }


}
