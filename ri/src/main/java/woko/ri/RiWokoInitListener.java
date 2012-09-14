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

package woko.ri;

import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hbcompass.HibernateCompassWokoInitListener;
import woko.hibernate.HibernateStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

@Deprecated
public class RiWokoInitListener extends HibernateCompassWokoInitListener {

    private static final WLogger logger = WLogger.getLogger(RiWokoInitListener.class);

    public static final String CTX_PARAM_WDEVEL_USERNAME = "Woko.Wdevel.Username";
    public static final String CTX_PARAM_WDEVEL_PASSWORD = "Woko.Wdevel.Password";

    @Override
    protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
        return new SessionUsernameResolutionStrategy();
    }

    @Override
    protected UserManager createUserManager() {
        HibernateUserManager um = new HibernateUserManager((HibernateStore)getObjectStore());
        String wdevelUsername = getServletContext().getInitParameter(CTX_PARAM_WDEVEL_USERNAME);
        if (wdevelUsername!=null) {
            um.setDeveloperUsername(wdevelUsername);
        } else {
            logger.warn("You seem to be using the default wdevel username : change this in web.xml using init param " + CTX_PARAM_WDEVEL_USERNAME + ", or even better : subclass UserManager");
        }
        String wdevelPassword = getServletContext().getInitParameter(CTX_PARAM_WDEVEL_PASSWORD);
        if (wdevelPassword!=null) {
            um.setDeveloperPassword(wdevelPassword);
        } else {
            logger.warn("You seem to be using the default wdevel password : change this in web.xml using init param " + CTX_PARAM_WDEVEL_PASSWORD + ", or even better : subclass UserManager");
        }
        um.createDefaultUsers();
        return um;
    }
}
