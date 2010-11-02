package woko2.hibernate

import woko2.persistence.ObjectStore
import org.hibernate.Session
import org.hibernate.SessionFactory
import woko2.util.WLogger
import org.hibernate.cfg.AnnotationConfiguration
import net.sourceforge.stripes.util.ResolverUtil
import org.hibernate.annotations.Entity
import org.hibernate.cfg.Configuration
import org.hibernate.metadata.ClassMetadata
import org.hibernate.Transaction
import woko2.persistence.ResultIterator
import woko2.persistence.ListResultIterator
import org.hibernate.Criteria

class HibernateStore implements ObjectStore {

  public static final String DEFAULT_HIBERNATE_CFG_XML = "/woko_default_hibernate.cfg.xml"

  private static final WLogger log = WLogger.getLogger(HibernateStore.class)

  private final HibernatePrimaryKeyConverter primaryKeyConverter
  private final SessionFactory sessionFactory
  private List<Class<?>> mappedClasses;

  HibernateStore(List<String> packageNames) {
    log.info("Creating with package names : $packageNames")
    Configuration cfg = createConfiguration(packageNames)
    log.info("Configuration created, building session factory...")
    URL u = getClass().getResource('/hibernate.cfg.xml')
    if (u==null) {
      log.warn("Using default hibernate settings from $DEFAULT_HIBERNATE_CFG_XML : in-memory hsql. Add your own hibernate.cfg.xml to the classpath to change the settings.")
      u = getClass().getResource(DEFAULT_HIBERNATE_CFG_XML)
    }
    sessionFactory = cfg.configure(u).buildSessionFactory()
    log.info("Created session factory : " + sessionFactory)
    primaryKeyConverter = createPrimaryKeyConverter()
    log.info("Created PK converter : " + primaryKeyConverter)
    int nbClasses = mappedClasses.size()
    if (nbClasses) {
      log.info("${mappedClasses.size()} persistent class(es) added.")
    } else {
      log.warn("No mapped classes found for packages $packageNames. Make sure your @Entity classes are in these packages.")
    }
  }

  SessionFactory getSessionFactory() {
    return sessionFactory
  }

  protected HibernatePrimaryKeyConverter createPrimaryKeyConverter() {
    return new HibernatePrimaryKeyConverter()
  }

  protected Configuration createConfiguration(List<String> packageNames) {
    mappedClasses = []
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
        log.info("  * $clazz added to config")
    }
    return cfg
  }

  Session getSession() {
    return sessionFactory.currentSession
  }

  def load(String className, String key) {
    log.debug("Loading object for className $className, key=$key")
    if (className==null && key==null) {
      return null
    }
    Class<?> mappedClass = getMappedClass(className)
    if (mappedClass==null) {
      return null
    }
    if (key==null) {
      // create transient instance
      return mappedClass.newInstance()
    }

    Class<?> keyType = getPrimaryKeyClass(mappedClass)
    if (keyType==null) {
      log.warn("Unable to get key type for mappedClass $mappedClass")
      return null
    }
    def id = primaryKeyConverter.convert(key, keyType)
    if (id==null) {
      log.warn("Converted key $key to null, unable to load any object")
      return null
    }
    Session s = getSession()
    Transaction tx = s.getTransaction()
    log.debug("Using transaction $tx")
    def o = s.get(mappedClass, id)
    return o
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

  String getKey(Object obj) {
    if (obj==null) {
      return null
    }
    def k = primaryKeyConverter.getPrimaryKeyValue(sessionFactory, obj)
    if (k==null) {
      return null
    }
    return k.toString()
  }

  String getClassMapping(Class<?> clazz) {
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

  Class<?> getMappedClass(String className) {
    if (className==null) {
      return null
    }
    // try forName first
    try {
      return Class.forName(className)
    } catch(ClassNotFoundException e) {
      // lookup simple name
      for (Class<?> clazz : mappedClasses) {
        String simpleName = clazz.getSimpleName()
        if (simpleName.equals(className)) {
          return clazz
        }
      }
      return null
    }
  }

  Class<?> getPrimaryKeyClass(Class<?> entityClass) {
    ClassMetadata cm = sessionFactory.getClassMetadata(entityClass)
    if (cm==null) {
      // default to String
      return String.class
    } else {
      return cm.identifierType.returnedClass
    }
  }

  ResultIterator list(String className, Integer start, Integer limit) {
    Class clazz = getMappedClass(className)
    int s = start==null ? 0 : start
    int l = limit==null ? -1 : limit
    if (clazz==null) {
      return new ListResultIterator([], start, limit, 0)
    } else {
      Criteria crit = session.createCriteria(clazz).setFirstResult(s)
      if (l!=-1) {
        crit.setMaxResults(l)
      }
      List objects = crit.list()

      // compute total count
      String mappedClassName = getClassMapping(clazz)
      int count = session.createQuery("select count(*) from $mappedClassName").list().get(0)

      return new ListResultIterator(objects, s, l, count)
    }
  }

  List<Class<?>> getMappedClasses() {
    return Collections.unmodifiableList(mappedClasses)
  }


}
