package woko.persistence;

import java.util.List;

public interface ObjectStore {

  Object load(String className, String key);

  Object save(Object obj);

  Object delete(Object obj);

  String getKey(Object obj);

  String getClassMapping(Class<?> clazz);

  Class<?> getMappedClass(String className);

  ResultIterator list(String className, Integer start, Integer limit);

  List<Class<?>> getMappedClasses();

  ResultIterator search(Object query, Integer start, Integer limit);

  void close();

}