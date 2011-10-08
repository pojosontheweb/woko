package woko.gae;

import woko.WokoInitListener;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import java.util.List;

public abstract class GaeWokoInitListener extends WokoInitListener {

  public static final String CTX_PARAM_PACKAGE_NAMES = "Woko.Gae.Packages";

  private static final WLogger logger = WLogger.getLogger(GaeWokoInitListener.class);

  @Override
  protected ObjectStore createObjectStore() {
    List<String> packageNames = getPackageNamesFromConfig(CTX_PARAM_PACKAGE_NAMES);
    logger.info("Scanning packages for Entities : " + packageNames);
    return new GaeStore(packageNames);
  }

}
