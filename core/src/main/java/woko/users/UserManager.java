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
import java.util.List;

/**
 * Woko UserManager interface : abstraction of the underlying users management system.
 *
 * No User type is provided on purpose : we want to keep the API very simple and lightweight.
 * Thereby the <code>UserManager</code> contract is very simple and manipulates basic types only.
 */
public interface UserManager {

    /**
     * Return the roles for passed username
     * @param username the username to get roles for
     * @return the roles for passed username, if any
     */
    List<String> getRoles(String username);

    /**
     * Authenticates (or not) passed username. Grabs additional credentials info (such as password)
     * from request and uses underlying system to perform authentication.
     * @param username the username
     * @param request the request
     * @return <code>true</code> if the user has been succesfuly authenticated, <code>false</code> otherwise
     */
    boolean authenticate(String username, HttpServletRequest request);

}