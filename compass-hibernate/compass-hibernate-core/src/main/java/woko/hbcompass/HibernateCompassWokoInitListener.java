package woko.hbcompass;

import woko.hibernate.HibernateWokoInitListener;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import java.util.List;

public abstract class HibernateCompassWokoInitListener extends HibernateWokoInitListener {

  private static final WLogger logger = WLogger.getLogger(HibernateCompassWokoInitListener.class);

  protected ObjectStore createObjectStore() {
    List<String> packageNames = getPackageNamesFromConfig(CTX_PARAM_PACKAGE_NAMES);
    logger.info("Scanning packages for Entities : " + packageNames);
    return new HibernateCompassStore(packageNames);
  }

}
