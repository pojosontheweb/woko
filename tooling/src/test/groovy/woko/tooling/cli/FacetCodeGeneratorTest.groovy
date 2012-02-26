package woko.tooling.cli

import org.junit.Test
import some.test.pkg.DummyBaseClass

class FacetCodeGeneratorTest {

    private String doGenerate(FacetCodeGenerator fcg) {
        def out = new ByteArrayOutputStream()
        out.withWriter { sysout ->
            fcg.setUseGroovy(true).generate(sysout)
        }
        return out.toString()
    }

    private void assertGeneratedText(FacetCodeGenerator fcg, String expectedText) {
        String actualText = doGenerate(fcg)
        assert actualText == expectedText
    }

    @Test
    void testGroovyFacetNoSupertypesNoFragmentNoTargetType() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass"),
            """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="myfacet", profileId="myrole")
class MyFacetClass {

}""")
    }

    @Test
    void testGroovyFacetNoSupertypesNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo"),
            """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass {

}""")
    }

    @Test
    void testGroovyFacetBaseClassNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setBaseClass(DummyBaseClass.class),
            """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo
import some.test.pkg.DummyBaseClass

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass extends DummyBaseClass {

}""")
    }

    @Test
    void testGroovyFacetBaseClassFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setBaseClass(DummyBaseClass.class).
                setFragmentPath("/WEB-INF/jsp/myrole/myjsp.jsp"),
            """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo
import some.test.pkg.DummyBaseClass

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass extends DummyBaseClass {

    @Override
    String getPath() {
        "/WEB-INF/jsp/myrole/myjsp.jsp"
    }

}""")
    }


}
