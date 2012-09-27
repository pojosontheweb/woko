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

package woko.facets.builtin.developer;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Delete;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {
                "facet.confirm",
                "facet.cancel"
        }
)
@FacetKey(name = WokoFacets.delete, profileId = "developer")
public class DeleteImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements Delete {

    public static final String TARGET_FACET_AFTER_DELETE = WokoFacets.home;

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/confirmDelete.jsp";
    private String confirm;
    private String cancel;

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    private static StreamingResolution createResolution(boolean success) {
        return new StreamingResolution("text/json", "{ \"success\": " + success + " }");
    }

    public Resolution getResolution(final ActionBeanContext abc) {
        if (cancel != null) {
            WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
            abc.getMessages().add(new LocalizableMessage("woko.devel.delete.cancel"));
            return new RpcResolutionWrapper(new RedirectResolution(
                    facetContext.getWoko().facetUrl(
                            WokoFacets.view,
                            facetContext.getTargetObject()))) {
                @Override
                public Resolution getRpcResolution() {
                    return createResolution(false);
                }
            };
        }
        if (confirm != null) {
            doDelete(abc);
            Resolution resolution = getNonRpcResolution(abc);
            return new RpcResolutionWrapper(resolution) {
                @Override
                public Resolution getRpcResolution() {
                    return createResolution(true);
                }
            };
        }
        // not confirmed, we display the confirm screen
        return new RpcResolutionWrapper(new ForwardResolution(FRAGMENT_PATH)) {
            @Override
            public Resolution getRpcResolution() {
                return createResolution(false);
            }
        };
    }

    protected Resolution getNonRpcResolution(ActionBeanContext abc) {
        WokoFacetContext<OsType,UmType,UnsType,FdmType> fc = getFacetContext();
        return new RedirectResolution(fc.getWoko().facetUrl(getTargetFacetAfterDelete(),fc.getTargetObject()));
    }

    protected String getTargetFacetAfterDelete() {
        return TARGET_FACET_AFTER_DELETE;
    }

    protected void doDelete(ActionBeanContext abc){
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        facetContext.getWoko().getObjectStore().delete(facetContext.getTargetObject());
        abc.getMessages().add(new LocalizableMessage("woko.devel.delete.confirm"));
    }




}
