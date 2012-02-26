package woko.tooling.cli

import org.junit.Test
import some.test.pkg.DummyBaseClass
import woko.facets.builtin.RenderPropertyValue

abstract class BaseFacetCodeGeneratorTest {

    abstract boolean useGroovy();

    private String doGenerate(FacetCodeGenerator fcg) {
        def out = new ByteArrayOutputStream()
        out.withWriter { sysout ->
            fcg.setUseGroovy(useGroovy()).generate(sysout)
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
            getExpectedNoSupertypesNoFragmentNoTargetType()
        )
    }

    protected abstract String getExpectedNoSupertypesNoFragmentNoTargetType();

    @Test
    void testGroovyFacetNoSupertypesNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo"),
            getExpectedNoSupertypesNoFragment()
        )
    }

    protected abstract String getExpectedNoSupertypesNoFragment();

    @Test
    void testGroovyFacetBaseClassNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setBaseClass(DummyBaseClass.class),
            getExpectedBaseClassNoFragment())
    }

    protected abstract String getExpectedBaseClassNoFragment();

    @Test
    void testGroovyFacetBaseClassFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setBaseClass(DummyBaseClass.class).
                setFragmentPath("/WEB-INF/jsp/myrole/myjsp.jsp"),
            getExpectedBaseClassFragment()
        )
    }

    protected abstract String getExpectedBaseClassFragment()

    @Test
    void testGroovyFacetInterfaceFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setInterface(RenderPropertyValue.class).
                setFragmentPath("/WEB-INF/jsp/myrole/myjsp.jsp"),
            getExpectedInterfaceFragment()
        )
    }

    protected abstract String getExpectedInterfaceFragment()


}
