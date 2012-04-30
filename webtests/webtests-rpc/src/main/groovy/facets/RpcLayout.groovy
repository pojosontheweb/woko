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

import woko.facets.builtin.all.LayoutAll
import net.sourceforge.jfacets.annotations.FacetKey

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 04/03/12
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
@FacetKey(name="layout", profileId="all")
class RpcLayout extends LayoutAll {

    @Override
    String getAppTitle() {
        return "RPC tests"
    }


}
