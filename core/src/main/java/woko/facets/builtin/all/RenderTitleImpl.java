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
import net.sourceforge.stripes.util.ReflectUtil;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Generic <code>renderTitle</code> facet, assigned to <code>Object</code>.
 *
 * Looks for properties that could be used to render the title :
 * <ul>
 *     <li>title</li>
 *     <li>name</li>
 *     <li>id</li>
 *     <li>_id</li>
 * </ul>
 *
 * Defaults to <code>toString()</code> on the target object if no candidate property
 * could be found.
 */
@FacetKey(name = WokoFacets.renderTitle, profileId = "all")
public class RenderTitleImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderTitle {

    private static final WLogger logger = WLogger.getLogger(RenderTitleImpl.class);

    /**
     * Candidate properties
     */
    public static final List<String> PROP_NAMES =
            Collections.unmodifiableList(Arrays.asList("title", "name", "id", "_id"));

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderTitle.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getTitle() {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        Object o = facetContext.getTargetObject();
        if (o == null) {
            return "null";
        }
        String result = null;
        if (o instanceof Map) {
            Map m = (Map) o;
            for (String s : PROP_NAMES) {
                if (m.containsKey(s)) {
                    Object obj = m.get(s);
                    if (obj != null) {
                        result = obj.toString();
                    }
                    break;
                }
            }
        } else {
            for (String s : PROP_NAMES) {
                try {
                    PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(o.getClass(), s);
                    if (pd != null) {
                        Method readMethod = pd.getReadMethod();
                        if (readMethod != null) {
                            Object obj = pd.getReadMethod().invoke(o);
                            if (obj != null) {
                                result = obj.toString();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Error while getting prop value for property '" + s + "' of instance of '" + o.getClass() + "', ignored this property for title", e);
                }
            }
        }

        if (result != null) {
            return result;
        }

        // nothing matched, compute a meaningful title
        OsType objectStore = facetContext.getWoko().getObjectStore();
        String className = objectStore.getClassMapping(o.getClass());
        String key = objectStore.getKey(o);
        if (className != null && key != null) {
            return key + "@" + className;
        }
        return o.toString();
    }

}
