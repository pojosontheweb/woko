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

package woko.tooling.cli

class FacetCodeGeneratorJavaTest extends BaseFacetCodeGeneratorTest {

    @Override
    boolean useGroovy() {
        false
    }

    @Override
    protected String getExpectedNoSupertypesNoFragmentNoTargetType() {
        """package com.xyz;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="myfacet", profileId="myrole")
public class MyFacetClass {

}"""
    }

    @Override
    protected String getExpectedNoSupertypesNoFragment() {
        """package com.xyz;

import net.sourceforge.jfacets.annotations.FacetKey;
import com.xyz.MyPojo;

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
public class MyFacetClass {

}"""
    }

    @Override
    protected String getExpectedBaseClassNoFragment() {
        """package com.xyz;

import net.sourceforge.jfacets.annotations.FacetKey;
import com.xyz.MyPojo;
import some.test.pkg.DummyBaseClass;

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
public class MyFacetClass extends DummyBaseClass {

}"""
    }

    @Override
    protected String getExpectedBaseClassFragment() {
        """package com.xyz;

import net.sourceforge.jfacets.annotations.FacetKey;
import com.xyz.MyPojo;
import some.test.pkg.DummyBaseClass;

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
public class MyFacetClass extends DummyBaseClass {

    @Override
    public String getPath() {
        return "/WEB-INF/jsp/myrole/myjsp.jsp";
    }

}"""
    }

    @Override
    protected String getExpectedInterfaceFragment() {
        """package com.xyz;

import net.sourceforge.jfacets.annotations.FacetKey;
import com.xyz.MyPojo;
import woko.facets.builtin.RenderPropertyValue;

@FacetKey(name="myfacet", profileId="myrole", targetObjectType=MyPojo.class)
public class MyFacetClass implements RenderPropertyValue {

    @Override
    public String getPath() {
        return "/WEB-INF/jsp/myrole/myjsp.jsp";
    }

}"""
    }

}
