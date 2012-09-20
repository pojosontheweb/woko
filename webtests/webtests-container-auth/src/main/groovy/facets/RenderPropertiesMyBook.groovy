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

package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.all.RenderPropertiesImpl
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.users.RemoteUserStrategy
import woko.inmemory.InMemoryUserManager
import woko.hibernate.HibernateStore

@FacetKey(name="renderProperties", profileId="all", targetObjectType=MyBook.class)
class RenderPropertiesMyBook extends RenderPropertiesImpl<HibernateStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> {

    @Override
    List<String> getPropertyNames() {
        def props = []
        props.addAll(super.getPropertyNames())
        props << "notexistingprop" // add non existing prop to test that is doesn't crash !
        return props
    }

}
