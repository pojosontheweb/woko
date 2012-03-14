/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderTitleImpl;

@FacetKey(name= WokoFacets.renderTitle, profileId="all", targetObjectType = User.class)
public class RenderUserTitle extends RenderTitleImpl {

    @Override
    public String getTitle() {
        User u = (User)getFacetContext().getTargetObject();
        if (u==null) {
            throw new IllegalStateException("user is null, cannot invoke renderTitle facet on it.");
        }
        return u.getUsername();
    }
}
