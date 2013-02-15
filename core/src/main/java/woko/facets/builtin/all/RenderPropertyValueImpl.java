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
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderPropertiesEdit;
import woko.facets.builtin.RenderPropertyValue;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;

/**
 * Generic <code>renderPropertyValue</code> facet. Assigned to target type <code>Object</code>, it
 * acts as a fallback when no other, more specific <code>renderPropertyValue</code> facet could be
 * found for the property.
 *
 * If the property is a Woko-managed POJO, then displays the property as a link to the object. Otherwise,
 * just call <code>toString()</code> on the property value and display that.
 *
 * This class can also be used as a base class when you write your own
 * <code>renderPropertyValue</code> facets.
 */
@FacetKey(name = WokoFacets.renderPropertyValue, profileId = "all")
public class RenderPropertyValueImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderPropertyValue {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValue.jsp";
    private Object owningObject;
    private String propertyName;
    private Object propertyValue;

    public Object getOwningObject() {
        return owningObject;
    }

    public void setOwningObject(Object owningObject) {
        this.owningObject = owningObject;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public Class<?> getPropertyType() {
        return Util.getPropertyType(getOwningObject().getClass(), getPropertyName());
    }

    /**
     * Introduced to maintain backward compat for "renderPropertyValueEdit" facets extending RenderPropertyValueImpl.
     * Those classes should now extend BaseRenderPropertyValueEdit.
     *
     * This method is deprecated and will be removed in next major release. Will be replaced by
     * {@link woko.facets.builtin.all.BaseRenderPropertyValueEdit#getFieldPrefix()}
     *
     * @return "object"
     */
    @Deprecated
    public String getFieldPrefix() {
        return "object";
    }
}
