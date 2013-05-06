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
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.facets.builtin.RenderPropertyValueEdit;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * <code>renderPropertyValueEdit</code> for properties of type <code>Date</code> : input field
 * with date formatting and calendar widget.
 */
@FacetKeyList(
        keys = {
                @FacetKey(name = WokoFacets.renderPropertyValueEdit_mail, profileId = "all"),
                @FacetKey(name = WokoFacets.renderPropertyValueEdit_email, profileId = "all")
        }
)

public class RenderPropertyValueEditMail<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseRenderPropertyValueEdit<OsType,UmType,UnsType,FdmType> implements RenderPropertyValueEdit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderPropertyValueEditMail.jsp";

    public static String TITLE_EXAMPLE = "example: foo@pojosontheweb.com";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getTitleExample(){
        return TITLE_EXAMPLE;
    }


}
