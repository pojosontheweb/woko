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
import org.json.JSONArray;
import org.json.JSONObject;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * <code>renderPropertyValueJson</code> for properties of type <code>Collection</code>. Converts the
 * property value to a JSON array and uses <code>renderPropertyValueJson</code> for the items contained.
 */
@FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all", targetObjectType = Collection.class)
public class RenderPropertyValueJsonCollection<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFacet<OsType, UmType, UnsType, FdmType> implements RenderPropertyValueJson {

    private static final WLogger logger = WLogger.getLogger(RenderPropertyValueJsonCollection.class);

    /**
     * Return the property value as a <code>JSONArray</code>
     * @param request the request
     * @param propertyValue the property value
     * @return the property value as a <code>JSONArray</code>
     */
    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        JSONArray arr = new JSONArray();
        Collection<?> c = (Collection<?>) propertyValue;
        for (Object item : c) {
            if (logger.isDebugEnabled()) {
                logger.debug("converting collection item $item to json...");
            }
            if (item == null) {
                arr.put(new JSONObject());
            }
            RenderPropertyValueJson rpvj = getFacetContext().getWoko().getFacet(WokoFacets.renderPropertyValueJson, request, item);
            if (rpvj == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("... no renderPropertyValueJson facet found for collection item, adding empty JSON Object");
                }
                arr.put(new JSONObject());
            } else {
                Object json = rpvj.propertyToJson(request, item);
                if (logger.isDebugEnabled()) {
                    logger.debug("... converted item $item to $json");
                }
                arr.put(json);
            }
        }
        return arr;
    }

}
