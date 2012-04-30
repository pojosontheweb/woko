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

package woko.push

import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import static junit.framework.Assert.*
import org.junit.Test
import net.sourceforge.jfacets.IFacetDescriptorManager
import net.sourceforge.jfacets.FacetDescriptor

class PushFacetDescriptorManagerTest {

    AnnotatedFacetDescriptorManager createAnnotated() {
        AnnotatedFacetDescriptorManager afdm =
            new AnnotatedFacetDescriptorManager(["woko.push.testfacets"]).
            initialize()
        assertEquals("invalid initial fd count in @fdm", 1, afdm.descriptors.length)
        return afdm
    }

    PushFacetDescriptorManager createPush() {
        PushFacetDescriptorManager push = new PushFacetDescriptorManager(createAnnotated())
        assertEquals("invalid initial fd count in push fdm", 1, push.descriptors.length)
        return push
    }

    static final FACET_SOURCE =  """package foo.bar

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="newFacet", profileId="all")
class MyNewFacet {
}

"""

    FacetDescriptor assertDescriptor(IFacetDescriptorManager fdm, String name, String profileId) {
        def fd = fdm.getDescriptor(name, profileId, Object.class)
        assertNotNull("facet descriptor not found for " + name + "," + profileId + ",Object", fd)
        return fd
    }

    void assertFacetClass(IFacetDescriptorManager fdm, String expectedClassName, String name, String profileId) {
        def fd = assertDescriptor(fdm, name, profileId)
        assertEquals("unexpected facet class for "+ name + "," + profileId + ",Object", expectedClassName, fd.facetClass.name)
    }

    void assertDescriptorCount(IFacetDescriptorManager fdm, int expected) {
        assertEquals("invalid fd count", expected, fdm.descriptors.length)
    }


    @Test
    void testReloadNewFacet() {
        def push = createPush()
        push.reload([FACET_SOURCE])
        assertDescriptorCount(push, 2)
        assertDescriptor(push, "my", "all")
        assertFacetClass(push, "foo.bar.MyNewFacet", "newFacet", "all")
    }

    @Test
    void testReloadReplaceFacetFromDelegate() {
        def push = createPush()
        push.reload(["""package foo.bar

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="my", profileId="all")
class My2 {
}

"""])
        assertDescriptorCount(push, 1)
        assertFacetClass(push, "foo.bar.My2", "my", "all")
    }


    @Test
    void testReloadReplaceFacetTwice() {

        def push = createPush()
        push.reload(["""package foo.bar

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="my", profileId="all")
class My2 {
}

"""])
        assertDescriptorCount(push, 1)
        assertFacetClass(push, "foo.bar.My2", "my", "all")

        push.reload(["""package foo.bar

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="my", profileId="all")
class My3 {
}

"""])

        assertDescriptorCount(push, 1)
        assertFacetClass(push, "foo.bar.My3", "my", "all")
    }

    @Test
    void testReloadNoSources() {
        def push = createPush()
        push.reload([])
        assertDescriptorCount(push, 1)
        assertFacetClass(push, "woko.push.testfacets.MyFacet", "my", "all")
    }


}
