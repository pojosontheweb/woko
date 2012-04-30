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

import woko.hbcompass.HibernateCompassInMemWokoInitListener
import net.sourceforge.jfacets.IFacetDescriptorManager
import woko.push.PushFacetDescriptorManager
import woko.persistence.ObjectStore
import woko.hibernate.HibernateStore
import woko.hibernate.TxCallback

class ContainerAuthWTInitIListener extends HibernateCompassInMemWokoInitListener {

    @Override
    protected IFacetDescriptorManager createFacetDescriptorManager() {
        return new PushFacetDescriptorManager(super.createFacetDescriptorManager()) // Enable /push !
    }

    @Override
    protected ObjectStore createObjectStore() {
        HibernateStore o = super.createObjectStore()
        o.doInTx({ store, session ->
            EntityWithRelations ewr = new EntityWithRelations(name:"test")
            store.save(ewr)
            SubEntity se = new SubEntity(name:"testSub")
            se.daEntity = ewr
            store.save(se)
        } as TxCallback)
        return o
    }


}
