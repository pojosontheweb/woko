package woko.tooling.cli

import org.junit.Test
import some.test.pkg.DummyBaseClass
import woko.facets.builtin.RenderPropertyValue

class FacetCodeGeneratorGroovyTest extends BaseFacetCodeGeneratorTest {

    @Override
    boolean useGroovy() {
        true
    }

    @Override
    protected String getExpectedNoSupertypesNoFragmentNoTargetType() {
        """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="myfacet", profileId="myrole")
class MyFacetClass {

}"""
    }

    @Override
    protected String getExpectedNoSupertypesNoFragment() {
        """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass {

}"""
    }

    @Override
    protected String getExpectedBaseClassNoFragment() {
        """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo
import some.test.pkg.DummyBaseClass

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass extends DummyBaseClass {

}"""
    }

    @Override
    protected String getExpectedBaseClassFragment() {
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

}"""
    }

    @Override
    protected String getExpectedInterfaceFragment() {
        """package com.xyz

import net.sourceforge.jfacets.annotations.FacetKey
import com.xyz.MyPojo
import woko.facets.builtin.RenderPropertyValue

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
class MyFacetClass implements RenderPropertyValue {

    @Override
    String getPath() {
        "/WEB-INF/jsp/myrole/myjsp.jsp"
    }

}"""
    }

}
