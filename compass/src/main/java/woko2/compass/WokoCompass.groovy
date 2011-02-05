package woko2.compass

import org.compass.core.config.CompassConfiguration
import org.compass.core.Compass
import woko2.util.WLogger
import net.sourceforge.stripes.util.ResolverUtil
import org.compass.annotations.Searchable
import org.compass.core.CompassSession
import org.compass.core.CompassTransaction

class WokoCompass {

  private static final WLogger log = WLogger.getLogger(WokoCompass.class)

  private final List<String> packageNames
  private Compass compass

  public WokoCompass(List<String> packageNames) {
    log.info("Creating with package names : $packageNames")
    this.packageNames = packageNames
    compass = buildCompass()
  }

  protected CompassConfiguration createCompassConfig(List<Class<?>> mappedClasses) {
    CompassConfiguration cc
    // try compass.cfg.xml
    URL u = getClass().getResource("/compass.cfg.xml")
    if (u!=null) {
      log.info("Using Compass config in /compass.cfg.xml")
      cc = new CompassConfiguration().configure(u)
    } else {
      // not found, use our default in-mem
      log.warn("No Compass config found using /woko_default_compass.cfg.xml (in-mem). Place /compass.cfg.xml in your classpath to change the compass settings.")
      u = getClass().getResource("/woko_default_compass.cfg.xml")
      cc = new CompassConfiguration().configure(u)
    }
    // add classes
    log.info("Adding classes...")
    return addClasses(cc, mappedClasses)
  }

  protected CompassConfiguration addClasses(CompassConfiguration cc, List<Class<?>> mappedClasses) {
    int nbAdded = 0
    mappedClasses.each { Class<?> clazz ->
      log.info("  -> Class $clazz added to compass")
      cc.addClass(clazz)
      nbAdded++
    }
    log.info("Added $nbAdded mapped entities")
    return cc
  }

  private Compass buildCompass() {
    // find classes to index in specified packages
    ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>()
    String[] packages = new String[packageNames.size()]
    packages = packageNames.toArray(packages)
    resolverUtil.findAnnotated(Searchable.class, packages)
    List<Class<?>> classes = new ArrayList<Class<?>>(resolverUtil.getClasses())
    return createCompassConfig(classes).buildCompass()
  }

  public Compass getCompass() {
    return compass
  }

  public def doInSession(Closure closure) {
    CompassSession s = compass.openSession()
    CompassTransaction tx = s.beginTransaction()
    try {
      closure.call(s)
      tx.commit()
    } catch(Exception e) {
      tx.rollback()
    } finally {
      s.close()
    }
  }

}
