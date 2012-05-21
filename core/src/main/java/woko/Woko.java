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

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.JFacets;
import net.sourceforge.jfacets.JFacetsBuilder;
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.DuplicatedKeyPolicyType;
import woko.facets.FacetNotFoundException;
import woko.facets.WokoFacetContextFactory;
import woko.facets.WokoProfileRepository;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.Util;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Woko {

    public static final String ENVI_FILE = "woko.environment";

    public static final List<String> DEFAULT_FACET_PACKAGES =
            Collections.unmodifiableList(Arrays.asList("facets", "woko.facets.builtin"));

    public static final String ROLE_ALL = "all";
    public static final String ROLE_GUEST = "guest";

    public static final WLogger logger = WLogger.getLogger(Woko.class);

    public static final String CTX_KEY = "woko";
    public static final String REQ_ATTR_FACET = "facet";

    private final UserManager userManager;
    private final ObjectStore objectStore;
    private final List<String> fallbackRoles;
    private final IFacetDescriptorManager facetDescriptorManager;
    private UsernameResolutionStrategy usernameResolutionStrategy;

    public static Woko getWoko(ServletContext ctx) {
        return (Woko) ctx.getAttribute(CTX_KEY);
    }

    protected JFacets jFacets;

    public Woko(ObjectStore objectStore,
                UserManager userManager,
                List<String> fallbackRoles,
                IFacetDescriptorManager facetDescriptorManager,
                UsernameResolutionStrategy usernameResolutionStrategy) {
        this.objectStore = objectStore;
        this.userManager = userManager;
        this.fallbackRoles = Collections.unmodifiableList(fallbackRoles);
        this.facetDescriptorManager = facetDescriptorManager;
        this.usernameResolutionStrategy = usernameResolutionStrategy;
        init();
    }

    private final Woko init() {
        logger.info("Initializing Woko...");
        initJFacets();
        customInit();
        logger.info("");
        logger.info("__       __     _  __");
        logger.info("\\ \\  _  / /___ | |/ / ___");
        logger.info(" \\ \\/ \\/ // o \\|   K /   \\");
        logger.info("  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0");
        logger.info("             POJOs on the Web !");
        logger.info("");
        logger.info("Woko is ready :");
        logger.info(" * userManager : " + userManager);
        logger.info(" * objectStore : " + objectStore);
        logger.info(" * jFacets : " + jFacets);
        logger.info(" * fallbackRole : " + fallbackRoles);
        logger.info(" * usernameResolutionStrategy : " + usernameResolutionStrategy);
        return this;
    }

    protected void initJFacets() {
        logger.info("Initializing JFacets...");
        WokoProfileRepository profileRepository = new WokoProfileRepository(userManager);
        WokoFacetContextFactory facetContextFactory = new WokoFacetContextFactory(this);
        jFacets = new JFacetsBuilder(profileRepository, facetDescriptorManager).
                setFacetContextFactory(facetContextFactory).
                build();
        FacetDescriptor[] descriptors = jFacets.getFacetRepository().getFacetDescriptorManager().getDescriptors();
        logger.info(descriptors.length + " facets found :");
        for (FacetDescriptor d : descriptors) {
            logger.info("  * " + d.getName() + ", " + d.getProfileId() + ", " + d.getTargetObjectType() + " -> " + d.getFacetClass());
        }
        logger.info("JFacets init OK.");
    }

    protected void customInit() {
    }

    public List<String> getFallbackRoles() {
        return fallbackRoles;
    }

    public ObjectStore getObjectStore() {
        return objectStore;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public JFacets getJFacets() {
        return jFacets;
    }

    public final void close() {
        logger.info("Closing...");
        doClose();
        logger.info("Woko has been closed.");
    }

    protected void doClose() {
        this.getObjectStore().close();
    }

    public Object getFacet(String name, HttpServletRequest request, Object targetObject) {
        return getFacet(name, request, targetObject, null);
    }

    public Object getFacet(String name, HttpServletRequest request, Object targetObject, Class targetObjectClass, boolean throwIfNotFound) {
        Object f = getFacet(name, request, targetObject, targetObjectClass);
        if (f == null && throwIfNotFound) {
            throw new FacetNotFoundException(name, targetObject, targetObjectClass, getUsername(request));
        }
        return f;
    }

    public Object getFacet(String name, HttpServletRequest request, Object targetObject, Class targetObjectClass) {
        logger.debug("Trying to get facet " + name + " for target object " + targetObject + ", targetObjectClass " + targetObjectClass + "...");
        String username = getUsername(request);
        List<String> roles;
        if (username == null) {
            roles = fallbackRoles;
            logger.debug("Username not supplied, using fallback roles : " + fallbackRoles);
        } else {
            roles = userManager.getRoles(username);
            if (roles == null || roles.size() == 0) {
                logger.debug("No roles returned for user '" + username + "', using fallback roles : " + fallbackRoles);
                roles = fallbackRoles;
            }
            logger.debug("Using roles " + roles + " for user " + username);
        }
        if (!roles.contains(ROLE_ALL)) {
            roles = new ArrayList<String>(roles);
            roles.add(ROLE_ALL);
        }
        if (targetObject == null && targetObjectClass == null) {
            logger.debug("No object or class provided, defaulting to Object.class");
            targetObjectClass = Object.class;
        }
        if (targetObjectClass == null && targetObject != null) {
            targetObjectClass = targetObject.getClass();
        }
        for (String role : roles) {
            logger.debug("Trying role : " + role);
            Object facet = jFacets.getFacet(name, role, targetObject, targetObjectClass);
            if (facet != null) {
                request.setAttribute(name, facet);
                request.setAttribute(REQ_ATTR_FACET, facet);
                logger.debug("Facet found and bound to request with name '" + name + "', returning " + facet);
                return facet;
            }
        }
        logger.debug("Facet not found for name: " + name + ", roles: " + roles + ", targetObject: " + targetObject + ", targetObjectClass: " + targetObjectClass + ", returning null");
        return null;
    }

    public String getUsername(HttpServletRequest request) {
        return usernameResolutionStrategy.getUsername(request);
    }

    public String facetUrl(String facetName, Object obj) {
        String className = objectStore.getClassMapping(obj.getClass());
        String id = objectStore.getKey(obj);
        StringBuilder sb = new StringBuilder("/").
                append(facetName).
                append("/").
                append(className);
        if (id != null) {
            sb.append("/").append(id);
        }
        return sb.toString();
    }

    public UsernameResolutionStrategy getUsernameResolutionStrategy() {
        return usernameResolutionStrategy;
    }

    public static IFacetDescriptorManager createFacetDescriptorManager(List<String> packageNames) {
        return createFacetDescriptorManager(packageNames, Woko.class.getClassLoader());
    }

    public static IFacetDescriptorManager createFacetDescriptorManager(List<String> packageNames, ClassLoader classLoader) {
        Util.assertArg("packageNames", packageNames);
        Util.assertArg("classLoader", classLoader);
        logger.info("Creating Annotated Facets, scanning packages : " + packageNames);
        return new AnnotatedFacetDescriptorManager(packageNames)
                .setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins)
                .setClassLoader(classLoader)
                .initialize();
    }


    /**
     * Return the environment used
     * @return the environment used at build-time (as found in the woko.environment file)
     */
    public String getEnvironment() {
        InputStream is = getClass().getResourceAsStream("/" + ENVI_FILE);
        if (is==null) {
            return null;
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        try {
            try {
                return r.readLine();
            } finally {
                r.close();
            }
        } catch(Exception e) {
            throw new RuntimeException("unable to read envi file !", e);
        }
    }


}
