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

package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.RenderPropertiesEdit;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@FacetKey(name = WokoFacets.renderPropertiesEdit, profileId = "all")
public class RenderPropertiesEditImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertiesImpl<OsType,UmType,UnsType,FdmType> implements RenderPropertiesEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertiesEdit.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    @Override
    public boolean isPartialForm() {
        return false;
    }

    @Override
    public String getFieldPrefix() {
        return "object";
    }

    @Override
    public Map<String, Object> getHiddenFields() {
        Map<String,Object> res = new HashMap<String, Object>();
        if (!isPartialForm()) {
            res.put("createTransient", null);
        }
        return res;
    }
}
