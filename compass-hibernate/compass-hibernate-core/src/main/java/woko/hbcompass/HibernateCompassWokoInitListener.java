package woko.hbcompass;

import woko.hibernate.HibernateWokoInitListener;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.util.WLogger;

import java.util.List;

public abstract class HibernateCompassWokoInitListener extends HibernateWokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Hibernate.Packages";

  private static final WLogger logger = WLogger.getLogger(HibernateCompassWokoInitListener.class);

  protected ObjectStore createObjectStore() {
    List<String> packageNames = getPackageNamesFromConfig();
    return new HibernateCompassStore(packageNames);
  }

  protected abstract UserManager createUserManager();

}
