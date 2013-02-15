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

import net.sourceforge.jfacets.*;
import woko.Woko;
import woko.actions.WokoRequestInterceptor;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * Woko-specific facet context factory. Creates <code>WokoFacetContext</code>s.
 */
public class WokoFacetContextFactory<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements IFacetContextFactory {

    private final Woko<OsType, UmType, UnsType, FdmType> woko;

    public WokoFacetContextFactory(Woko<OsType, UmType, UnsType, FdmType> woko) {
        this.woko = woko;
    }

    /**
     * Create and return the facet context for passed parameters. Uses
     * <code>WokoRequestInterceptor</code> in order to ser the request for
     * the context.
     * @param name the facet name
     * @param profile the profile
     * @param targetObject the target object
     * @param facetDescriptor the facet descriptor
     * @return a freshly created <code>WokoFacetContext</code>
     */
    public IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
        return new WokoFacetContext<OsType, UmType, UnsType, FdmType>(
                name,
                profile,
                targetObject,
                facetDescriptor,
                woko,
                WokoRequestInterceptor.getRequest());
    }


}
