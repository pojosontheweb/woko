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

package woko;

import woko.ioc.WokoIocContainer;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Arrays;
import java.util.List;

public abstract class WokoIocInitListener implements ServletContextListener {

    private static final WLogger logger = WLogger.getLogger(WokoIocInitListener.class);

    private ServletContext servletContext;

    public final void contextInitialized(ServletContextEvent e) {
        servletContext = e.getServletContext();
        servletContext.setAttribute(Woko.CTX_KEY, createWoko());
    }

    public final void contextDestroyed(ServletContextEvent e) {
        Woko woko = Woko.getWoko(e.getServletContext());
        if (woko != null) {
            woko.close();
        }
    }

    protected Woko createWoko() {
        WokoIocContainer ioc = createIocContainer(servletContext);
        return new Woko(ioc, createFallbackRoles());
    }

    protected abstract WokoIocContainer createIocContainer(ServletContext servletContext);

    protected List<String> createFallbackRoles() {
        return Arrays.asList(Woko.ROLE_GUEST);
    }


}
