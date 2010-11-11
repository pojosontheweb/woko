package woko2.hibernate

import woko2.WokoInitListener
import woko2.Woko
import woko2.users.UserManager
import woko2.persistence.ObjectStore
import woko2.util.WLogger
import woko2.users.UsernameResolutionStrategy
import woko2.users.RemoteUserStrategy

abstract class HibernateWokoInitListener extends WokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages"

  private static final WLogger logger = WLogger.getLogger(HibernateWokoInitListener.class)

  Woko createWoko() {
    return new Woko(createObjectStore(), createUserManager(), createFallbackRoles(), Woko.createDefaultFacetDescriptorManager(), Woko.createDefaultUsernameResolutionStrategy())
  }

  protected List<String> createFallbackRoles() {
    return [Woko.ROLE_GUEST]
  }

  protected ObjectStore createObjectStore() {
    def packageNames = getPackageNamesFromConfig()
    return new HibernateStore(packageNames)
  }

  List<String> getPackageNamesFromConfig() {
    String pkgNamesStr = servletContext.getInitParameter(CTX_PARAM_PACKAGE_NAMES)
    if (!pkgNamesStr) {
      def msg = "No package names specified. You have to set the context init-param $CTX_PARAM_PACKAGE_NAMES in web.xml to the list of packages you want to be scanned for pesistent classes."
      logger.error(msg)
      throw new IllegalStateException(msg)
    }
    String[] pkgNamesArr = pkgNamesStr.
            replace('\n', ',').
            replace(' ', ',').
            split(',')
    def pkgNames = []
    for (String s : pkgNamesArr) {
      if (s) {
        pkgNames << s
      }
    }
    return pkgNames
  }

  protected abstract UserManager createUserManager()

}
