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

import woko.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.validation.Validate
import net.sourceforge.stripes.action.SimpleMessage
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.users.RemoteUserStrategy
import woko.inmemory.InMemoryUserManager
import woko.hbcompass.HibernateCompassStore

@FacetKey(name="testValidate", profileId="all")
class TestValidate extends BaseResolutionFacet<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager> {

  @Validate(required=true)
  String prop

  Resolution getResolution(ActionBeanContext abc) {
    abc.messages.add(new SimpleMessage("you have entered $prop"))
    return new ForwardResolution("/facet-validation-test.jsp")
  }


}
