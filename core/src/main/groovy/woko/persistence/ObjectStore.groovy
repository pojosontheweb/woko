package woko.persistence

public interface ObjectStore {

  def load(String className, String key)

  def save(def obj)

  def delete(def obj)

  String getKey(obj)

  String getClassMapping(Class<?> clazz)

  Class<?> getMappedClass(String className)
  
  ResultIterator list(String className, Integer start, Integer limit)

  List<Class<?>> getMappedClasses()

  ResultIterator search(def query, Integer start, Integer limit)

}