package woko.hibernate;

import woko.Woko;
import woko.WokoInitListener;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.util.WLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class HibernateWokoInitListener extends WokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages";

  private static final WLogger logger = WLogger.getLogger(HibernateWokoInitListener.class);

  protected Woko createWoko() {
    return new Woko(createObjectStore(),
        createUserManager(),
        createFallbackRoles(),
        Woko.createDefaultFacetDescriptorManager(),
        Woko.createDefaultUsernameResolutionStrategy());
  }

  protected List<String> createFallbackRoles() {
    return Arrays.asList(Woko.ROLE_GUEST);
  }

  protected ObjectStore createObjectStore() {
    List<String> packageNames = getPackageNamesFromConfig();
    return new HibernateStore(packageNames);
  }

  public List<String> getPackageNamesFromConfig() {
    String pkgNamesStr = getServletContext().getInitParameter(CTX_PARAM_PACKAGE_NAMES);
    if (pkgNamesStr==null || pkgNamesStr.equals("")) {
      String msg = "No package names specified. You have to set the context init-param " +
          CTX_PARAM_PACKAGE_NAMES + " in web.xml to the list of packages you want to be scanned for pesistent classes.";
      logger.error(msg);
      throw new IllegalStateException(msg);
    }
    String[] pkgNamesArr = pkgNamesStr.
            replace('\n', ',').
            replace(' ', ',').
            split(",");
    List<String> pkgNames = new ArrayList<String>();
    for (String s : pkgNamesArr) {
      if (s!=null && !s.equals("")) {
        pkgNames.add(s);
      }
    }
    return pkgNames;
  }

  protected abstract UserManager createUserManager();

}
