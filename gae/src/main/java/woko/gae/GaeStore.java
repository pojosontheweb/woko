package woko.gae;

import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.ResolverUtil;
import woko.persistence.ListResultIterator;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.util.WLogger;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static woko.util.Util.assertArg;

public class GaeStore implements ObjectStore {

  private final List<Class<?>> mappedClasses = new ArrayList<Class<?>>();

  private static final WLogger log = WLogger.getLogger(GaeStore.class);

  public GaeStore(List<String> packageNames) {
    if (packageNames==null) {
        packageNames = Arrays.asList("model");
    }
    log.info("Adding mapped classes from : " + packageNames);
    ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
    String[] packages = new String[packageNames.size()];
    packages = packageNames.toArray(packages);
    resolverUtil.findAnnotated(Entity.class, packages);
    for (Class<?> clazz : resolverUtil.getClasses()) {
        mappedClasses.add(clazz);
        log.info("  * " + clazz + " added");
    }
    Collections.sort(mappedClasses, new Comparator<Class<?>>() {
      @Override
      public int compare(Class<?> aClass, Class<?> aClass1) {
        return aClass.getSimpleName().compareTo(aClass1.getSimpleName());
      }
    });
  }

  private EntityManager getEm() {
    return GaeEntityManagerInterceptor.getEntityManager();
  }

  @Override
  public Object load(String className, final String key) {
    final Class<?> clazz = getMappedClass(className);
    if (clazz==null) {
      return null;
    }
    if (key==null) {
      // create transient instance
      try {
        return clazz.newInstance();
      } catch (Exception e) {
        log.error("Unable to create instance of " + clazz + " using no-args constructor.", e);
        throw new RuntimeException(e);
      }
    }
    // TODO grab the primary key type and convert it
    return getEm().find(clazz, key);
  }

  @Override
  public Object save(final Object o) {
    getEm().persist(o);
    return o;
  }

  @Override
  public Object delete(Object o) {
    getEm().remove(o);
    return o;
  }

  private String getFieldValueString(Object target, Field field) {
    assertArg("target", target);
    assertArg("field", field);
    Object retval;
    try {
      retval = field.get(target);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("unable to obtain value of field " + field.getName() +
        " for object " + target + " of class " + target.getClass(), e);
    }
    if (retval==null) {
      return null;
    }
    return retval.toString();
  }

  private String invokeReadMethodString(Object target, Method method) {
    assertArg("target", target);
    assertArg("method", method);
    Object retVal;
    try {
      retVal = method.invoke(target);
    } catch (Exception e) {
      throw new RuntimeException("unable to get value for method " + method.getName() + " of object " +
          target + " of class " + target.getClass(), e);
    }
    if (retVal==null) {
      return null;
    }
    return retVal.toString();
  }

  @Override
  public String getKey(Object o) {
    if (o==null) {
      return null;
    }
    // grab the annotation on the class for the moment
    // TODO introspect JPA metadata ?
    Class<?> clazz = o.getClass();
    // first look for fields...
    Collection<Field> fields = ReflectUtil.getFields(clazz);
    Field idField = null;
    for (Field f : fields) {
      if (f.isAnnotationPresent(Id.class)) {
        idField = f;
        break;
      }
    }
    if (idField!=null) {
      String idFieldName = idField.getName();
      // look of we have a property for this name
      PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(clazz, idFieldName);
      if (pd==null) {
        // no prop descriptor found, let's try to read the field
        return getFieldValueString(o, idField);
      } else {
        // property descriptor found, we use the read method
        Method readMethod = pd.getReadMethod();
        if (readMethod==null) {
          // read method not available try the field anyway
          return getFieldValueString(o, idField);
        } else {
          // invoke the read method
          return invokeReadMethodString(o, readMethod);
        }
      }
    } else {
      // found no annotated field, try the properties (annotated getter)
      PropertyDescriptor[] properties = ReflectUtil.getPropertyDescriptors(clazz);
      Method idPropReadMethod = null;
      for (PropertyDescriptor pd : properties) {
        Method readMethod = pd.getReadMethod();
        if (readMethod.isAnnotationPresent(Id.class)) {
          idPropReadMethod = readMethod;
          break;
        }
      }
      if (idPropReadMethod==null) {
        return null;
      }
      return invokeReadMethodString(o, idPropReadMethod);
    }
  }

  @Override
  public String getClassMapping(Class<?> aClass) {
    assertArg("aClass", aClass);
    return aClass.getSimpleName();
  }

  @Override
  public Class<?> getMappedClass(String className) {
    if (className==null) {
      return null;
    }
    for (Class<?> mc : getMappedClasses()) {
      String fqcn = mc.getName();
      if (fqcn.equals(className) || fqcn.endsWith(className)) {
        return mc;
      }
    }
    return null;
  }

  @Override
  public ResultIterator list(String className, Integer start, Integer limit) {
    Class<?> clazz = getMappedClass(className);
    Query query = getEm().createQuery("select o from " + clazz.getSimpleName() + " o");
    int iStart = start!=null ? start : 0;
    int iLimit = limit!=null ? limit : Integer.MAX_VALUE;
    List results = query.setFirstResult(iStart).setMaxResults(iLimit).getResultList();
    // we need the count as well
    Number count = (Number)getEm().createQuery(
        "select count(o) from " + className + " o").getSingleResult();
    return new ListResultIterator(results, iStart, iLimit, count.intValue());
  }

  @Override
  public List<Class<?>> getMappedClasses() {
    return mappedClasses;
  }

  @Override
  public ResultIterator search(Object o, Integer integer, Integer integer1) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public void close() {
    // nothing to do
  }
}
