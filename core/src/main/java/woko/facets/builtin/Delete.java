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

package woko.facets.builtin;

import woko.facets.ResolutionFacet;

/**
 * <code>delete</code> facet : used to delete managed POJOs with urls like :
 *
 * <pre>http://.../delete/MyClass/123</pre>
 */
public interface Delete extends ResolutionFacet {

    static final String FACET_NAME = "delete";

    /**
     * Confirmation flag : actually delete the object if <code>true</code>
     * @return <code>true</code> if the target object has to be deleted
     */
    Boolean getConfirm();

    /**
     * Cancel flag : cancel deletion of the object if <code>true</code>
     * @return <code>true</code> if the operation is cancelled
     */
    Boolean getCancel();

}