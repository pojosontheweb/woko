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
import net.sourceforge.stripes.util.ReflectUtil;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.RenderTitleEdit;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Generic <code>renderTitleEdit</code> facet, assigned to <code>Object</code>.
 * Used to render the titles for edited objects.
 */
@FacetKey(name = WokoFacets.renderTitleEdit, profileId = "all")
public class RenderTitleEditImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderTitleImpl<OsType,UmType,UnsType,FdmType> implements RenderTitleEdit {
}
