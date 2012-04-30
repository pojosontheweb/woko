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

package woko.util

import org.junit.Test

class UtilTest {

    private void assertProps(o) {
        def pNames = Util.getPropertyNames(o, ['p2'])
        println pNames
        ['p1', 'p3', 'p4'].each {
            assert pNames.contains(it)
        }
        assert !pNames.contains('p2')
    }

    @Test
    void testGetPropertyNamesOnObject() {
        assertProps(new DummyPojo())
    }

    @Test
    void testGetPropertyGenericTypes() {
        assert Util.getPropertyGenericTypes(DummyWithCollections.class, "stringCollection")[0] == String.class
        assert Util.getPropertyGenericTypes(DummyPojo.class, "stringList")[0] == String.class
    }


}
