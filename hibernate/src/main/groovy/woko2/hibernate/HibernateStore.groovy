package woko2.hibernate

import woko2.persistence.ObjectStore
import org.hibernate.Session
import org.hibernate.SessionFactory
import woko2.util.WLogger
import org.hibernate.cfg.AnnotationConfiguration
import net.sourceforge.stripes.util.ResolverUtil
import org.hibernate.annotations.Entity

class HibernateStore implements ObjectStore {

  private static final WLogger log = WLogger.getLogger(HibernateStore.class)

  private final PrimaryKeyConverter primaryKeyConverter
  private final SessionFactory sessionFactory
  private List<Class<?>> mappedClasses;

  HibernateStore(List<String> packageNames) {
    log.info("Creating with package names : $packageNames")
    this.sessionFactory = createSessionFactory(packageNames)
    this.primaryKeyConverter = createPrimaryKeyConverter()
  }

  SessionFactory getSessionFactory() {
    return sessionFactory
  }

  protected PrimaryKeyConverter createPrimaryKeyConverter() {
    return new PrimaryKeyConverter()
  }

  protected SessionFactory createSessionFactory(List<String> packageNames) {
    if (packageNames==null) {
        packageNames = ['model']
    }
    log.info("Creating hibernate annotation configuration")
    AnnotationConfiguration cfg = new AnnotationConfiguration()
    ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>()
    String[] packages = new String[packageNames.size()]
    packages = packageNames.toArray(packages)
    resolverUtil.findAnnotated(Entity.class, packages)
    resolverUtil.findAnnotated(javax.persistence.Entity.class, packages)
    for (Class<?> clazz : resolverUtil.getClasses()) {
        cfg.addAnnotatedClass(clazz)
        mappedClasses.add(clazz)
        log.info("Class $clazz added to config")
    }
    cfg = cfg.configure()
    log.info("Configuration created, building session factory...")
    SessionFactory sf = cfg.buildSessionFactory()
    log.info("Created session factory : " + sf)
    return sf
  }

  Session getSession() {
    return sessionFactory.currentSession
  }

  def load(String className, String key) {
    if (className==null && key==null) {
      return null
    }
    Class<?> mappedClass = getMappedClass(className)
    if (mappedClass==null) {
      return null
    }
    def id = primaryKeyConverter.convert(key, mappedClass)
    if (id==null) {
      return null
    }
    return session.load(mappedClass, id)
  }

  def save(Object obj) {
    if (obj==null) {
      return null
    }
    session.saveOrUpdate obj
    return obj
  }

  def delete(Object obj) {
    if (obj==null) {
      return null
    }
    session.delete obj
    return obj
  }

  def String getKey(Object obj) {
    if (obj==null) {
      return null
    }
    def k = primaryKeyConverter.getPrimaryKeyValue(sessionFactory, obj)
    if (k==null) {
      return null
    }
    return k.toString()
  }

  def String getClassMapping(Class<?> clazz) {
    // is the class persistent ?
    def classMetadata = sessionFactory.getClassMetadata(clazz)
    if (classMetadata==null) {
      // not a hibernated class, return the fully qualified class name
      return clazz.getName()
    } else {
      // hibernated class, return the simple name
      return clazz.getSimpleName()
    }
  }

  def Class<?> getMappedClass(String className) {
    if (className==null) {
      return null
    }
    // try forName first
    try {
      return Class.forName(className)
    } catch(ClassNotFoundException e) {
      // lookup simple name
      List<Class<?>> mappedClasses = HibernateInterceptor.getMappedClasses()
      for (Class<?> clazz : mappedClasses) {
        String simpleName = clazz.getSimpleName()
        if (simpleName.equals(className)) {
          return clazz
        }
      }
      return null
    }
  }


}
