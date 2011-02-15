package woko.hbcompass

import woko.persistence.ObjectStore
import woko.users.UserManager
import woko.util.WLogger
import woko.hibernate.HibernateWokoInitListener

abstract class HibernateCompassWokoInitListener extends HibernateWokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages"

  private static final WLogger logger = WLogger.getLogger(HibernateCompassWokoInitListener.class)

  protected ObjectStore createObjectStore() {
    def packageNames = getPackageNamesFromConfig()
    return new HibernateCompassStore(packageNames)
  }

  protected abstract UserManager createUserManager()

}
