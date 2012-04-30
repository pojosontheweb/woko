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

package woko.tooling.cli

import org.junit.Test
import some.test.pkg.DummyBaseClass
import woko.facets.builtin.RenderPropertyValue

abstract class BaseFacetCodeGeneratorTest {

    abstract boolean useGroovy();

    private String doGenerate(FacetCodeGenerator fcg) {
        def out = new ByteArrayOutputStream()
        out.withWriter { sysout ->
            fcg.
              setUseGroovy(useGroovy()).
              generate(sysout)
        }
        return out.toString()
    }

    private void assertGeneratedText(FacetCodeGenerator fcg, String expectedText) {
        String actualText = doGenerate(fcg)
        assert actualText == expectedText
    }

    @Test
    void testFacetNoSupertypesNoFragmentNoTargetType() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass"),
            getExpectedNoSupertypesNoFragmentNoTargetType()
        )
    }

    protected abstract String getExpectedNoSupertypesNoFragmentNoTargetType();

    @Test
    void testFacetNoSupertypesNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo"),
            getExpectedNoSupertypesNoFragment()
        )
    }

    protected abstract String getExpectedNoSupertypesNoFragment();

    @Test
    void testFacetBaseClassNoFragment() {
        assertGeneratedText(
            new FacetCodeGenerator(null, new File(""), "myfacet", "myrole", "com.xyz.MyFacetClass").
                setTargetObjectType("com.xyz.MyPojo").
                setBaseClass(DummyBaseClass.class),
            getExpectedBaseClassNoFragment())
    }

    protected abstract String getExpectedBaseClassNoFragment();

    @Test
    void testFacetBaseClassFragment() {
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
    void testFacetInterfaceFragment() {
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
