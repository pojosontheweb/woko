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

package woko.users;

import javax.servlet.http.HttpServletRequest;

/**
 * Username resolution strategy that uses the request's remote user as the username,
 * for use with container authentication.
 */
public class RemoteUserStrategy implements UsernameResolutionStrategy {

    /**
     * Return the request's remote user
     * @param request the request
     * @return the remote user
     */
    public String getUsername(HttpServletRequest request) {
        return request.getRemoteUser();
    }

}
