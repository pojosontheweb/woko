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
import woko.WokoIocInitListener
import woko.ioc.WokoIocContainer
import javax.servlet.ServletContext
import org.picocontainer.DefaultPicoContainer
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import org.picocontainer.parameters.ConstantParameter
import woko.pico.WokoIocPico
import woko.inmemory.InMemoryUserManager
import woko.users.RemoteUserStrategy
import woko.hbcompass.HibernateCompassStore
import woko.Woko
import net.sourceforge.jfacets.annotations.DuplicatedKeyPolicyType

@Deprecated
class ContainerAuthWTInitIListener extends WokoIocInitListener<HibernateStore,InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> {

    @Override
    protected WokoIocContainer createIocContainer(ServletContext servletContext) {
        List<String> packagesNames = getPackageNamesFromConfig(CTX_PARAM_FACET_PACKAGES, false);
        List<String> pkgs = new ArrayList<String>();
        if (packagesNames != null && packagesNames.size() > 0) {
            pkgs.addAll(packagesNames);
        }
        pkgs.addAll(Woko.DEFAULT_FACET_PACKAGES);
        DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.addComponent(
                WokoIocContainer.FacetDescriptorManager,
                new AnnotatedFacetDescriptorManager(pkgs)
                    .setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins)
                    .initialize()
        );
        pico.addComponent(
                WokoIocContainer.ObjectStore,
                HibernateCompassStore.class,
                new ConstantParameter(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true))
        );
        pico.addComponent(
                WokoIocContainer.UserManager,
                new InMemoryUserManager().addUser("wdevel", "wdevel", Arrays.asList("developer"))
        );
        pico.addComponent(
                WokoIocContainer.UsernameResolutionStrategy,
                RemoteUserStrategy.class
        );
        pico.start();
        return new WokoIocPico(pico);
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
