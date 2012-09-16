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

package woko.actions;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public abstract class BaseActionBean<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements ActionBean {

    private WokoActionBeanContext<OsType, UmType, UnsType, FdmType> context;

    @SuppressWarnings("unchecked")
    public void setContext(ActionBeanContext context) {
        this.context = (WokoActionBeanContext<OsType, UmType, UnsType, FdmType>) context;
    }

    public WokoActionBeanContext<OsType, UmType, UnsType, FdmType> getContext() {
        return context;
    }

}
