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

package woko.facets.builtin.all

import org.junit.Test
import static RenderPropertyValueJsonDate.*

class RenderPropertyValueJsonDateTest {

    @Test
    void testDateToJsonString() {
        Date d = new Date()
        String s = dateToJsonString(d)
        assert isJsonDate(s)
    }

    @Test
    void testDateFromJsonString() {
        Date d1 = new Date()
        String s = "/Date(${d1.time})/"
        assert isJsonDate(s)
        Date d2 = dateFromJsonString(s)
        assert d1.equals(d2)
    }

}
