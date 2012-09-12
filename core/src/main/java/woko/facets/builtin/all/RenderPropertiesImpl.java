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

package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderProperties;
import woko.facets.builtin.WokoFacets;
import woko.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FacetKey(name = WokoFacets.renderProperties, profileId = "all")
public class RenderPropertiesImpl extends BaseFragmentFacet implements RenderProperties {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderProperties.jsp";
    public static final String FRAGMENT_PATH_FLAT = "/WEB-INF/woko/jsp/all/renderPropertiesFlatLayout.jsp";

    private List<String> propertyNames;
    private Map<String, Object> propertyValues;
    private boolean useFlatLayout = false;

    public boolean isUseFlatLayout() {
        return useFlatLayout;
    }

    public void setUseFlatLayout(boolean useFlatLayout) {
        this.useFlatLayout = useFlatLayout;
    }

    public String getPath() {
        if (useFlatLayout) {
            return FRAGMENT_PATH_FLAT;
        }
        return FRAGMENT_PATH;
    }

    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public Map<String, Object> getPropertyValues() {
        return propertyValues;
    }

    public void setFacetContext(IFacetContext iFacetContext) {
        super.setFacetContext(iFacetContext);
        Object obj = iFacetContext.getTargetObject();
        Class<?> objType = obj!=null ? obj.getClass() : iFacetContext.getFacetDescriptor().getTargetObjectType();
        propertyNames = Util.getPropertyNames(objType, Arrays.asList("metaClass"));
        propertyValues = new HashMap<String, Object>();
        if (obj!=null) {
            for (String pName : propertyNames) {
                propertyValues.put(pName, Util.getPropertyValue(obj, pName));
            }
        }
    }

}
