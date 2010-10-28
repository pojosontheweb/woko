package woko2.inmemory

import woko2.persistence.ObjectStore

class InMemoryObjectStore implements ObjectStore {

  def objects = new HashMap<String,Map<String,Object>>()

  private void addObject(String className, String key, def obj) {
    def objectsForClass = objects[className]
    if (objectsForClass==null) {
      objectsForClass = new HashMap<String,Object>()
      objects[className] = objectsForClass
    }
    objectsForClass[key] = obj
  }

  def load(String className, String key) {
    def objectsForClass = objects[className]
    if (objectsForClass==null) {
      return null
    }
    return objectsForClass[key]      
  }

  def save(obj) {
    assert obj
    String key = getKey(obj)
    assert key
    String className = getClassName(obj)
    assert className
    addObject className, key, obj
  }

  String getKey(obj) {
    assert obj
    try {
      def id = obj._id
      if (id==null) {
        return null
      }
      return id.toString()
    } catch(MissingPropertyException e) {
      throw new IllegalStateException("Unable to get id for object $obj : no _id property found.")
    }
  }

  def String getClassName(obj) {
    assert obj
    if (obj instanceof Map) {
      // dynamic
      try {
        return obj._type
      } catch(MissingPropertyException e) {
        throw new IllegalStateException("Unable to get className for $obj : it's dynamic, but has no _type property.")
      }
    }
    // static
    return obj.getClass().getName()
  }

  def Class<?> getMappedClass(String className) {
    try {
      return Class.forName(className)
    } catch(ClassNotFoundException e) {
      return Map.class
    }
  }

  def delete(obj) {
    assert obj
    String key = getKey(obj)
    assert key
    String className = getClassName(obj)
    assert className
    def objectsForClass = objects[className]
    if (objectsForClass) {
      return objectsForClass.remove(key)
    }
    return null
  }


}
