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
import net.sourceforge.stripes.controller.StripesFilter;
import woko.facets.FacetNotFoundException;
import woko.facets.WokoFacetContextFactory;
import woko.facets.WokoProfileRepository;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoInjectHelper;
import woko.ioc.WokoIocContainer;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;
import woko.util.Util;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;

/**
 * The infamous Woko !
 * This is the top-level component in a Woko application. It provides access to the various
 * components of the system ({@link ObjectStore}, {@link UserManager}, etc.) and to the facet
 * retrieval APIs.
 *
 * @param <OsType> the type of the ObjectStore
 * @param <UmType> the type of the UserManager
 * @param <UnsType> the type of the UsernameResolutionStrategy
 * @param <FdmType> the type of the IFacetDescriptorManager
 */
public class Woko<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements Closeable {

    public static final WLogger logger = WLogger.getLogger(Woko.class);

    /**
     * Environment file name
     */
    public static final String ENVI_FILE = "woko.environment";

    /**
     * Cached environment name
     */
    private static String ENVI = null;

    /**
     * List of the default facet packages
     */
    public static final List<String> DEFAULT_FACET_PACKAGES =
            Collections.unmodifiableList(Arrays.asList("facets", "woko.facets.builtin"));

    /**
     * Default role "all"
     */
    public static final String ROLE_ALL = "all";

    /**
     * Default role "guest"
     */
    public static final String ROLE_GUEST = "guest";

    /**
     * ServletContext attribute name for binding Woko
     */
    public static final String CTX_KEY = "woko";

    /**
     * facet request attribute name
     */
    public static final String REQ_ATTR_FACET = "facet";

    /**
     * Woko version
     */
    public static final String VERSION = "2.2";

    /**
     * Return the Woko instance for passed servletContext
     * @param ctx the servlet context
     * @return the Woko instance bound to the servlet context
     */
    @SuppressWarnings("unchecked")
    public static <
            OsType extends ObjectStore,
            UmType extends UserManager,
            UnsType extends UsernameResolutionStrategy,
            FdmType extends IFacetDescriptorManager> Woko<OsType,UmType,UnsType,FdmType> getWoko(ServletContext ctx) {
        return (Woko<OsType,UmType,UnsType,FdmType>)ctx.getAttribute(CTX_KEY);
    }

    /**
     * Reference to JFacets (created at init)
     */
    protected JFacets jFacets;

    /**
     * Reference to the IOC container (passed at init)
     */
    private WokoIocContainer<OsType,UmType,UnsType,FdmType> iocContainer = null;

    /**
     * List of the fallback role(s) (role(s) for unauthenticated users)
     */
    private List<String> fallbackRoles = null;

    /**
     * Create and initialize Woko with passed parameters
     * @deprecated use constructor that takes IOC container as argument
     */
    @Deprecated
    public Woko(OsType objectStore,
                UmType userManager,
                List<String> fallbackRoles,
                FdmType facetDescriptorManager,
                UnsType usernameResolutionStrategy) {
        iocContainer = new SimpleWokoIocContainer<OsType,UmType,UnsType,FdmType>(objectStore, userManager, usernameResolutionStrategy, facetDescriptorManager);
        this.fallbackRoles = fallbackRoles;
        init();
    }

    /**
     * Create and initialize Woko with passed parameters
     * @param ioc the IOC container
     * @param fallbackRoles the fallback role(s)
     */
    public Woko(WokoIocContainer<OsType,UmType,UnsType,FdmType> ioc, List<String> fallbackRoles) {
        this.iocContainer = ioc;
        this.fallbackRoles = fallbackRoles;
        init();
    }

    /**
     * Init JFacets and call customInit().
     */
    private void init() {
        logger.info("Initializing Woko...");
        initJFacets();
        customInit();
        logger.info("");
        logger.info("__       __     _  __");
        logger.info("\\ \\  _  / /___ | |/ / ___");
        logger.info(" \\ \\/ \\/ // o \\|   K /   \\");
        logger.info("  \\__W__/ \\___/|_|\\_\\\\_o_/  " + VERSION);
        logger.info("             POJOs on the Web !");
        logger.info("");
        logger.info("Woko is ready :");
        logger.info(" * userManager : " + getUserManager());
        logger.info(" * objectStore : " + getObjectStore());
        logger.info(" * jFacets : " + jFacets);
        logger.info(" * fallbackRole : " + fallbackRoles);
        logger.info(" * usernameResolutionStrategy : " + getUsernameResolutionStrategy());
    }

    /**
     * Initialize JFacets for the application (invoked at init).
     */
    protected void initJFacets() {
        logger.info("Initializing JFacets...");
        WokoProfileRepository profileRepository = new WokoProfileRepository(getUserManager(), true);
        WokoFacetContextFactory<OsType,UmType,UnsType,FdmType> facetContextFactory = new WokoFacetContextFactory<OsType,UmType,UnsType,FdmType>(this);
        jFacets = new JFacetsBuilder(profileRepository, getFacetDescriptorManager()).
                setFacetContextFactory(facetContextFactory).
                build();
        FacetDescriptor[] descriptors = jFacets.getFacetRepository().getFacetDescriptorManager().getDescriptors();
        logger.info(descriptors.length + " facets found :");
        for (FacetDescriptor d : descriptors) {
            logger.info("  * " + d.getName() + ", " + d.getProfileId() + ", " + d.getTargetObjectType() + " -> " + d.getFacetClass());
        }
        logger.info("JFacets init OK.");
    }

    /**
     * Post-init hook, called after JFacets init. Does nothing, by default.
     */
    protected void customInit() {
    }

    /**
     * Return the IOC container
     * @return the IOC container
     */
    public final WokoIocContainer<OsType,UmType,UnsType,FdmType> getIoc() {
        return iocContainer;
    }

    /**
     * Return the fallback roles, used when there is no user authenticated
     * @return the configured fallback roles
     */
    public final List<String> getFallbackRoles() {
        return Collections.unmodifiableList(fallbackRoles);
    }

    /**
     * Return the <code>ObjectStore</code> from IOC
     * @return the <code>ObjectStore</code> from IOC
     */
    public OsType getObjectStore() {
        return getIoc().getObjectStore();
    }

    /**
     * Return the <code>UserManager</code> from IOC
     * @return the <code>UserManager</code> from IOC
     */
    public UmType getUserManager() {
        return getIoc().getUserManager();
    }

    /**
     * Return the <code>IFacetDescriptorManager</code> from IOC
     * @return the <code>IFacetDescriptorManager</code> from IOC
     */
    public FdmType getFacetDescriptorManager() {
        return getIoc().getFacetDescriptorManager();
    }

    /**
     * Return the <code>UsernameResolutionStrategy</code> from IOC
     * @return the <code>UsernameResolutionStrategy</code> from IOC
     */
    public UnsType getUsernameResolutionStrategy() {
        return getIoc().getUsernameResolutionStrategy();
    }

    /**
     * Return the JFacets instance
     * @return the JFacets instance
     */
    public JFacets getJFacets() {
        return jFacets;
    }

    /**
     * Close Woko (close IOC).
     */
    public final void close() {
        logger.info("Closing...");
        WokoIocContainer<?,?,?,?> ioc = getIoc();
        if (ioc instanceof Closeable) {
            logger.info("Closing IOC...");
            ((Closeable)ioc).close();
        }
        doClose();
        logger.info("Woko has been closed.");
    }

    /**
     * Post-close hook, invoked after IOC has been closed.
     * Does nothing by default.
     */
    protected void doClose() {
    }

    /**
     * Return the facet for passed parameters.
     * @param name the facet name
     * @param request the request
     * @param targetObject the target object (not null)
     * @param <T> the facet type
     * @return the facet if any (<code>null</code> if no such facet exists)
     */
    @SuppressWarnings("unchecked")
    public <T> T getFacet(String name, HttpServletRequest request, Object targetObject) {
        return getFacet(name, request, targetObject, null);
    }

    /**
     * Return the facet for passed parameters
     * @param name the facet name
     * @param request the request
     * @param targetObject the target object (can be null)
     * @param targetObjectClass the target object class (not null)
     * @param throwIfNotFound throw a <code>FacetNotFoundException</code> if the facet is not found and this flag is <code>true</code>
     * @param <T> the type of the facet
     * @return the facet if found (or null or throws <code>FacetNotFoundException</code> depending on passed args)
     */
    public <T> T getFacet(String name, HttpServletRequest request, Object targetObject, Class<?> targetObjectClass, boolean throwIfNotFound) {
        T f = getFacet(name, request, targetObject, targetObjectClass);
        if (f == null && throwIfNotFound) {
            throw new FacetNotFoundException(name, targetObject, targetObjectClass, getUsername(request));
        }
        return f;
    }

    /**
     * Return the facet for passed parameters
     * @param name the facet name
     * @param request the request
     * @param targetObject the target object (can be null)
     * @param targetObjectClass the target object class (not null)
     * @param <T> the facet type
     * @return the facet if found, <code>null</code> otherwise
     */
    public <T> T getFacet(String name, HttpServletRequest request, Object targetObject, Class<?> targetObjectClass) {
        logger.debug("Trying to get facet " + name + " for target object " + targetObject + ", targetObjectClass " + targetObjectClass + "...");
        String username = getUsername(request);
        List<String> roles;
        if (username == null) {
            roles = fallbackRoles;
            logger.debug("Username not supplied, using fallback roles : " + fallbackRoles);
        } else {
            roles = getUserManager().getRoles(username);
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
            @SuppressWarnings("unchecked")
            T facet = (T)jFacets.getFacet(name, role, targetObject, targetObjectClass);
            if (facet != null) {
                request.setAttribute(name, facet);
                request.setAttribute(REQ_ATTR_FACET, facet);
                logger.debug("Facet found and bound to request with name '" + name + "', returning " + facet);

                // inject components for methods marked with @WokoInject
                WokoInjectHelper.injectComponents(this, facet);

                return facet;
            }
        }
        logger.debug("Facet not found for name: " + name + ", roles: " + roles + ", targetObject: " + targetObject + ", targetObjectClass: " + targetObjectClass + ", returning null");
        return null;
    }

    /**
     * Return the username for passed request, using configured
     * <code>UsernameResolutionStrategy</code>.
     * @param request the request
     * @return the username for the request if any, <code>null</code> otherwise
     */
    public String getUsername(HttpServletRequest request) {
        return getUsernameResolutionStrategy().getUsername(request);
    }

    /**
     * Return the URL for passed (resolution) facet name and target object
     * @param facetName the facet name
     * @param obj the target object
     * @return the URL to the resolution facet (like /view/MyClass/123)
     */
    public String facetUrl(String facetName, Object obj) {
        return "/" + LinkUtil.getUrl(this, obj, facetName);
    }

    /**
     * Create facet descriptor manager for passed package names and the app's classloader.
     * @param packageNames the facet package names
     * @return a freshly created and initialized <code>IFacetDescriptorManager</code>
     */
    public static IFacetDescriptorManager createFacetDescriptorManager(List<String> packageNames) {
        return createFacetDescriptorManager(packageNames, Woko.class.getClassLoader());
    }

    /**
     * Create facet descriptor manager for passed package names and classloader.
     * @param packageNames the facet package names
     * @param classLoader the classloader to be used for facet class lookup
     * @return a freshly created and initialized <code>IFacetDescriptorManager</code>
     */
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
     * Check if some environment has been used to build the app
     * @param ifNoEnv the value to return in case no environment was used at all (getEnvironment()==null)
     * @param envNames the name of the environments to check
     * @return true if the environment used matches passed parameter (null safe), false otherwise
     */
    public static boolean isUsingEnvironment(boolean ifNoEnv, String... envNames) {
        String actualEnv = getEnvironment();
        if (actualEnv==null) {
            return ifNoEnv;
        }
        for (String en : envNames) {
            if (actualEnv.equals(en)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the environment used
     * @return the environment used at build-time (as found in the woko.environment file)
     */
    public static String getEnvironment() {
        if (ENVI==null) {
            InputStream is = Woko.class.getResourceAsStream("/" + ENVI_FILE);
            if (is!=null) {
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                try {
                    try {
                        ENVI = r.readLine();
                    } finally {
                        r.close();
                    }
                } catch(Exception e) {
                    logger.error("Unable to read environment file at " + ENVI_FILE,e);
                }
            }
        }
        return ENVI;
    }

    /**
     * Return the localized message for passed key and args, looking up in the
     * configured resource bundles.
     * @param request the request
     * @param key the message key
     * @param args the message arguments
     * @return the formatted message
     */
    public String getLocalizedMessage(HttpServletRequest request, String key, String... args) {
        Locale locale = request.getLocale();
        return getLocalizedMessage(locale, key, args);
    }

    /**
     * Return the localized message for passed locale, key and args, looking up in the
     * configured resource bundles.
     * @param locale the locale
     * @param key the message key
     * @param args the message arguments
     * @return the formatted message
     */
    public String getLocalizedMessage(Locale locale, String key, String... args) {
        ResourceBundle b = StripesFilter.getConfiguration().
                getLocalizationBundleFactory().getFormFieldBundle(locale);
        try {
            String msgTemplate = b.getString(key);
            if (args.length==0) {
                return msgTemplate;
            }
            MessageFormat f = new MessageFormat(msgTemplate, locale);
            return f.format(args, new StringBuffer(), null).toString();
        } catch(MissingResourceException e) {
            return key;
        }
    }

}
