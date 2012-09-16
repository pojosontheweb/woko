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

public class WokoFacetContextFactory<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements IFacetContextFactory {

  private final Woko<OsType,UmType,UnsType,FdmType> woko;

  public WokoFacetContextFactory(Woko<OsType,UmType,UnsType,FdmType> woko) {
    this.woko = woko;
  }

  public IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
    return new WokoFacetContext<OsType,UmType,UnsType,FdmType>(
        name,
        profile,
        targetObject,
        facetDescriptor,
        woko,
        WokoRequestInterceptor.getRequest());
  }


}
