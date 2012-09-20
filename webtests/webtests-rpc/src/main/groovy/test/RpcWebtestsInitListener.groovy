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

package test


import woko.WokoIocInitListener
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.ioc.SimpleWokoIocContainer
import woko.ioc.WokoIocContainer
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.users.RemoteUserStrategy

class RpcWebtestsInitListener extends
        WokoIocInitListener<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager> {
    @Override
    protected WokoIocContainer<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager> createIocContainer() {
        return new SimpleWokoIocContainer<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager>(
                new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true)),
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer"]),
                new RemoteUserStrategy(),
                createAnnotatedFdm());
    }

}
