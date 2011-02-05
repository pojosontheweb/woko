package woko2.hbcompass

import woko2.persistence.ObjectStore
import woko2.users.UserManager
import woko2.util.WLogger
import woko2.hibernate.HibernateWokoInitListener

abstract class HibernateCompassWokoInitListener extends HibernateWokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages"

  private static final WLogger logger = WLogger.getLogger(HibernateCompassWokoInitListener.class)

  protected ObjectStore createObjectStore() {
    def packageNames = getPackageNamesFromConfig()
    return new HibernateCompassStore(packageNames)
  }

  protected abstract UserManager createUserManager()

}
