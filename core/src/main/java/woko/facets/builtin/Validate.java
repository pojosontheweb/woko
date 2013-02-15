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

import net.sourceforge.stripes.action.ActionBeanContext;

/**
 * <code>validate</code> facet is used to validate the state of a Woko managed POJO before
 * is is saved. It can perform some additional validation routines, e.g. using the database layer.
 */
public interface Validate {

    static final String FACET_NAME = "validate";

    /**
     * Check for validation constraints on the target object's state, and adds errors to the
     * action bean context if needed.
     * @param abc the action bean context to add errors to
     * @return <code>true</code> if there are no errors, <code>false</code> if some validation constraints were violated
     */
    boolean validate(ActionBeanContext abc);

}