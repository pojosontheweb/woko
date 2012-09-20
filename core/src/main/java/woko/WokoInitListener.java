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

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.RemoteUserStrategy;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Deprecated
public abstract class WokoInitListener implements ServletContextListener {

    public static final String CTX_PARAM_FACET_PACKAGES = "Woko.Facet.Packages";

    private static final WLogger logger = WLogger.getLogger(WokoInitListener.class);

    private ServletContext servletContext;

    private ObjectStore objectStore;
    private UserManager userManager;
    private List<String> fallbackRoles;
    private IFacetDescriptorManager facetDescriptorManager;
    private UsernameResolutionStrategy usernameResolutionStrategy;

    public ObjectStore getObjectStore() {
        return objectStore;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public List<String> getFallbackRoles() {
        return fallbackRoles;
    }

    public IFacetDescriptorManager getFacetDescriptorManager() {
        return facetDescriptorManager;
    }

    public UsernameResolutionStrategy getUsernameResolutionStrategy() {
        return usernameResolutionStrategy;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

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

    @SuppressWarnings("unchecked")
    protected Woko createWoko() {
        objectStore = createObjectStore();
        userManager = createUserManager();
        fallbackRoles = createFallbackRoles();
        facetDescriptorManager = createFacetDescriptorManager();
        usernameResolutionStrategy = createUsernameResolutionStrategy();
        return new Woko(
                objectStore,
                userManager,
                fallbackRoles,
                facetDescriptorManager,
                usernameResolutionStrategy);
    }

    protected List<String> createFallbackRoles() {
        return Arrays.asList(Woko.ROLE_GUEST);
    }

    protected IFacetDescriptorManager createFacetDescriptorManager() {
        List<String> pkgs = new ArrayList<String>();
        List<String> packagesNames = getPackageNamesFromConfig(CTX_PARAM_FACET_PACKAGES, false);
        if (packagesNames != null && packagesNames.size() > 0) {
            pkgs.addAll(packagesNames);
        }
        pkgs.addAll(Woko.DEFAULT_FACET_PACKAGES);
        return Woko.createFacetDescriptorManager(pkgs);
    }

    protected abstract ObjectStore createObjectStore();

    protected abstract UserManager createUserManager();

    protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
        return new RemoteUserStrategy();
    }

    protected List<String> getPackageNamesFromConfig(String paramName) {
        return getPackageNamesFromConfig(paramName, true);
    }

    protected List<String> getPackageNamesFromConfig(String paramName, boolean throwIfNotFound) {
        String pkgNamesStr = getServletContext().getInitParameter(paramName);
        if (pkgNamesStr == null || pkgNamesStr.equals("")) {
            if (throwIfNotFound) {
                String msg = "No package names specified. You have to set the context init-param '" +
                        paramName + "' in web.xml to the list of packages you want to be scanned.";
                logger.error(msg);
                throw new IllegalStateException(msg);
            } else {
                return Collections.emptyList();
            }
        }

        return extractPackagesList(pkgNamesStr);
    }

    public static List<String> extractPackagesList(String packagesStr) {
        String[] pkgNamesArr = packagesStr.
                replace('\n', ',').
                replace(' ', ',').
                split(",");
        List<String> pkgNames = new ArrayList<String>();
        for (String s : pkgNamesArr) {
            if (s != null && !s.equals("")) {
                pkgNames.add(s);
            }
        }
        return pkgNames;
    }

}
