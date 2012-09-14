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
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.DuplicatedKeyPolicyType;
import woko.ioc.WokoIocContainer;
import woko.persistence.ObjectStore;
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

public abstract class WokoIocInitListener<OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> implements ServletContextListener {

    public static final String CTX_PARAM_FACET_PACKAGES = "Woko.Facet.Packages";

    protected List<String> getPackageNamesFromConfig(String paramName, boolean throwIfNotFound) {
        String pkgNamesStr = servletContext.getInitParameter(paramName);
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


    private static final WLogger logger = WLogger.getLogger(WokoIocInitListener.class);

    private ServletContext servletContext;

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

    private Woko<OsType,UmType,UnsType,FdmType> createWoko() {
        WokoIocContainer ioc = createIocContainer();
        Woko<OsType,UmType,UnsType,FdmType> w = new Woko<OsType,UmType,UnsType,FdmType>(ioc, createFallbackRoles());
        postInit(w);
        return w;
    }

    protected abstract WokoIocContainer createIocContainer();

    protected List<String> createFallbackRoles() {
        return Arrays.asList(Woko.ROLE_GUEST);
    }

    protected void postInit(Woko<OsType,UmType,UnsType,FdmType> w) { }

    protected List<String> getFacetPackages() {
        List<String> packagesNames = getPackageNamesFromConfig(CTX_PARAM_FACET_PACKAGES, false);
        List<String> pkgs = new ArrayList<String>();
        if (packagesNames != null && packagesNames.size() > 0) {
            pkgs.addAll(packagesNames);
        }
        pkgs.addAll(Woko.DEFAULT_FACET_PACKAGES);
        return pkgs;
    }

    protected AnnotatedFacetDescriptorManager createAnnotatedFdm() {
        return new AnnotatedFacetDescriptorManager(getFacetPackages())
                .setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins)
                .initialize();
    }

}
