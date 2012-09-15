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

package woko.ext.usermanagement.core;

import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.util.Util;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class DatabaseUserManager<T extends DatabaseUserManager<T, U>, U extends User> implements UserManager {

    private static final WLogger logger = WLogger.getLogger(DatabaseUserManager.class);

    public static final String REQ_PARAM_NAME = "password";

    private String reqParamName = REQ_PARAM_NAME;
    private String wDevelUsername = "wdevel";
    private String wDevelPassword = "wdevel";

    private final Class<U> userClass;

    private static final List<String> DEFAULT_ROLES;

    static {
        ArrayList<String> defaultRoles = new ArrayList<String>();
        defaultRoles.add("developer");
        DEFAULT_ROLES = defaultRoles;
    }

    private List<String> defaultRoles = DEFAULT_ROLES;

    protected DatabaseUserManager(Class<U> userClass) {
        this.userClass = userClass;
    }

    public Class<U> getUserClass() {
        return userClass;
    }

    @SuppressWarnings("unchecked")
    public T setRequestParameterName(String reqParamName) {
        this.reqParamName = reqParamName;
        return (T)this;
    }

    public String getReqParamName() {
        return reqParamName;
    }

    public String getwDevelUsername() {
        return wDevelUsername;
    }

    public String getwDevelPassword() {
        return wDevelPassword;
    }

    @SuppressWarnings("unchecked")
    public T setDefaultRoles(List<String> defaultRoles) {
        this.defaultRoles = defaultRoles;
        return (T)this;
    }

    public List<String> getDefaultRoles() {
        return defaultRoles;
    }

    @SuppressWarnings("unchecked")
    public T setDeveloperUsername(String wDevelUsername) {
        this.wDevelUsername = wDevelUsername;
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T setDeveloperPassword(String wDevelPassword) {
        this.wDevelPassword = wDevelPassword;
        return (T)this;
    }

    @SuppressWarnings("unchecked")
    public T createDefaultUsers() {
        createUser(wDevelUsername, wDevelPassword, defaultRoles);
        return (T)this;
    }

    protected abstract U createUser(String username, String password, List<String> roles);

    @Override
    public List<String> getRoles(String username) {
        if (username==null) {
            return Collections.emptyList();
        }
        User u = getUserByUsername(username);
        if (u==null) {
            return Collections.emptyList();
        }
        List<String> roles = u.getRoles();
        if (roles==null) {
            return Collections.emptyList();
        }
        return roles;
    }

    public abstract U getUserByUsername(String username);

    @Override
    public boolean authenticate(String username, HttpServletRequest request) {
        if (username==null) {
            return false;
        }
        Util.assertArg("request", request);
        // retrieve user
        U u = getUserByUsername(username);
        if (u==null) {
            logger.warn("Authentication failure (no such user) for " + username);
            return false;
        }

        // check if account is active
        if (!u.getAccountStatus().equals(AccountStatus.Active)) {
            logger.warn("Authentication attempt on inactive account for " + username);
            return false;
        }

        // extract password from request and compare
        String clearPassword = extractPassword(request);
        String encodedPassword = encodePassword(clearPassword);

        String actualEncodedPassword = u.getPassword();
        boolean passwordsMatch = actualEncodedPassword != null && actualEncodedPassword.equals(encodedPassword);
        if (!passwordsMatch) {
            logger.warn("Authentication failure for " + username);
            return false;
        }
        logger.info("Authentication successful for " + username);
        return true;
    }


    public String extractPassword(HttpServletRequest request) {
        return request.getParameter(reqParamName);
    }

    public String encodePassword(String clearPassword) {
        Util.assertArg("clearPassword", clearPassword);
        return Integer.toString(clearPassword.hashCode());
    }

    public abstract ResultIterator<U> listUsers(Integer start, Integer limit);
}
