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

package net.sourceforge.stripes.rpc;

import net.sourceforge.stripes.action.Resolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract helper class for RPC resolutions.
 */
public abstract class RpcResolutionWrapper implements Resolution, RpcResolution {

    private final Resolution originalResolution;

    public RpcResolutionWrapper(Resolution originalResolution) {
        this.originalResolution = originalResolution;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        originalResolution.execute(request, response);
    }

}
