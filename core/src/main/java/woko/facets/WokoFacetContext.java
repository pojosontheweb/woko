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

package woko.facets;


import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.impl.DefaultFacetContext;
import woko.Woko;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Woko-specific <code>FacetContext</code>. Extension of JFacets' <code>DefaultFacetContext</code> that
 * provides access to the <code>Woko</code> instance as well as the <code>request</code>.
 */
public class WokoFacetContext<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends DefaultFacetContext {

    private final Woko<OsType, UmType, UnsType, FdmType> woko;
    private final HttpServletRequest request;

    /**
     * Create context with passed parameters
     * @param facetName the name of the facet
     * @param profile the profile requesting the facet
     * @param targetObject the target object
     * @param facetDescriptor the facet descriptor for the facet
     * @param woko the Woko instance
     * @param request the request
     */
    public WokoFacetContext(
            String facetName,
            IProfile profile,
            Object targetObject,
            FacetDescriptor facetDescriptor,
            Woko<OsType, UmType, UnsType, FdmType> woko,
            HttpServletRequest request) {
        super(facetName, profile, targetObject, facetDescriptor);
        this.woko = woko;
        this.request = request;
    }

    /**
     * Return the <code>Woko</code> instance
     * @return the <code>Woko</code> instance
     */
    public Woko<OsType, UmType, UnsType, FdmType> getWoko() {
        return woko;
    }

    /**
     * Return the request
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

}
