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

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Default <code>renderPropertyValueJson</code> facet for basic types <code>Number</code>,
 * <code>String</code> and <code>Boolean</code>
 */
@FacetKeyList(
        keys = {
                @FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all", targetObjectType = Number.class),
                @FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all", targetObjectType = String.class),
                @FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all", targetObjectType = Boolean.class)
        })
public class RenderPropertyValueJsonBasicTypes<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFacet<OsType, UmType, UnsType, FdmType> implements RenderPropertyValueJson {

    /**
     * Return the property value itself (no need for conversion here).
     * @param request the request
     * @param propertyValue the property value
     * @return <code>propertyValue</code>
     */
    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        // catch-all : return the target object itself
        return propertyValue;
    }


}
