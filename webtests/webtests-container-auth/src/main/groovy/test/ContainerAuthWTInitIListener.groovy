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

import woko.hibernate.HibernateStore
import woko.hibernate.TxCallback
import woko.ioc.WokoIocContainer
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import org.picocontainer.parameters.ConstantParameter
import woko.inmemory.InMemoryUserManager
import woko.users.RemoteUserStrategy
import woko.hbcompass.HibernateCompassStore
import woko.Woko
import static org.picocontainer.Characteristics.CACHE
import org.picocontainer.MutablePicoContainer
import woko.pico.WokoIocPicoInitListener

@Deprecated
class ContainerAuthWTInitIListener extends WokoIocPicoInitListener<HibernateStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> {

    @Override
    protected void addUsernameResolutionStrategy(MutablePicoContainer pico) {
        pico.as(CACHE).addComponent(WokoIocContainer.UsernameResolutionStrategy, RemoteUserStrategy.class);
    }

    @Override
    protected void addUserManager(MutablePicoContainer pico) {
        pico.addComponent(
            WokoIocContainer.UserManager,
            new InMemoryUserManager().addUser("wdevel", "wdevel", Arrays.asList("developer"))
        );
    }

    @Override
    protected void addObjectStore(MutablePicoContainer pico) {
        pico.as(CACHE).addComponent(
            WokoIocContainer.ObjectStore,
            HibernateCompassStore.class,
            new ConstantParameter(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true))
        );
    }

    @Override
    protected void postInit(Woko<HibernateStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> w) {
        w.getObjectStore().doInTx({ store, session ->
            EntityWithRelations ewr = new EntityWithRelations(id:1,name:"test")
            store.save(ewr)
            SubEntity se = new SubEntity(id:1,name:"testSub")
            se.daEntity = ewr
            store.save(se)
        } as TxCallback)
    }

}
