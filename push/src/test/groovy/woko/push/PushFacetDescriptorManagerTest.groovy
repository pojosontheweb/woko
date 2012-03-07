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
        def push = createPush().reload([FACET_SOURCE])
        assertDescriptorCount(push, 2)
        assertDescriptor(push, "my", "all")
        assertFacetClass(push, "foo.bar.MyNewFacet", "newFacet", "all")
    }

    @Test
    void testReloadReplaceFacetFromDelegate() {
        def push = createPush().reload(["""package foo.bar

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

        def push = createPush().reload(["""package foo.bar

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



}
