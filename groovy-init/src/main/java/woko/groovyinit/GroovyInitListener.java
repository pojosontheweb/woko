package woko.groovyinit;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.Woko;
import woko.WokoIocInitListener;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Alternative init listener that uses a Groovy script to startup Woko.
 * The <code>woko-init.groovy</code> script has to be available be placed at CLASSPATH root.
 * It has to return a fully configured Woko instance.
 * The only variable available to the binding of the init script is the <code>ServletContext</code>,
 * under the name <code>servletContext</code>.
 */
public class GroovyInitListener<OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> implements ServletContextListener {

    public static final String SCRIPT_FILE_NAME = "/woko-init.groovy";
    public static final String SERVLET_CONTEXT_BINDING_VAR = "servletContext";

    private static final WLogger logger = WLogger.getLogger(GroovyInitListener.class);

    private ServletContext servletContext;

    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Invoked at application startup. Creates Woko and binds to servlet context.
     */
    @Override
    public final void contextInitialized(ServletContextEvent e) {
        servletContext = e.getServletContext();
        logger.info("Initializing Woko");
        servletContext.setAttribute(Woko.CTX_KEY, createWoko());
    }

    /**
     * Close woko on application shutdown
     */
    @Override
    public final void contextDestroyed(ServletContextEvent e) {
        Woko woko = Woko.getWoko(e.getServletContext());
        if (woko != null) {
            woko.close();
        }
    }

    /**
     * Create and return a configured Woko by evaluating the woko-init.groovy script.
     */
    protected Woko<OsType, UmType, UnsType, FdmType> createWoko() {
        InputStream scriptStream = getClass().getResourceAsStream(SCRIPT_FILE_NAME);
        if (scriptStream==null) {
            logger.error("/woko-init.groovy not found in classpath, unable to startup !");
            throw new IllegalStateException("Could not find /woko-init.groovy script in your classpath : make sure the " +
                "script is there before using GroovyInitListener !");
        } else {
            logger.info("Starting-up using /woko-init.groovy found in classpath");
        }
        Reader scriptReader;
        try {
            scriptReader = new InputStreamReader(scriptStream, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Binding binding = new Binding();
        binding.setVariable(SERVLET_CONTEXT_BINDING_VAR, getServletContext());
        GroovyShell shell = new GroovyShell(binding);
        @SuppressWarnings("unchecked")
        Woko<OsType, UmType, UnsType, FdmType> res = (Woko<OsType, UmType, UnsType, FdmType>)shell.evaluate(scriptReader);
        return res;
    }

    /**
     * Static helper for grabbing package names from web.xml
     */
    public static List<String> getPackageNamesFromConfig(ServletContext servletContext, String paramName, boolean throwIfNotFound) {
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
        return WokoIocInitListener.extractPackagesList(pkgNamesStr);
    }

    /**
     * Static helper for grabbing facet package names from web.xml
     */
    public static List<String> getFacetPackagesFromWebXml(ServletContext servletContext) {
        List<String> packagesNames = getPackageNamesFromConfig(servletContext, WokoIocInitListener.CTX_PARAM_FACET_PACKAGES, false);
        List<String> pkgs = new ArrayList<String>();
        if (packagesNames != null && packagesNames.size() > 0) {
            pkgs.addAll(packagesNames);
        }
        pkgs.addAll(Woko.DEFAULT_FACET_PACKAGES);
        return pkgs;
    }



}
