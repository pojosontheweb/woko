package woko2.persistence

public interface ObjectStore {

  def load(String className, String key)

  def save(def obj)

  def delete(def obj)

  String getKey(obj)

  String getClassName(obj)

  Class<?> getMappedClass(String className)

}