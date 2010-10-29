package woko2.inmemory

import woko2.persistence.ObjectStore

class InMemoryObjectStore implements ObjectStore {

  def objects = new HashMap<String,Map<String,Object>>()

  public void addObject(def obj) {
    assert obj
    String className = getClassMapping(obj.getClass())
    String key = getKey(obj)
    def objectsForClass = objects[className]
    if (objectsForClass==null) {
      objectsForClass = new HashMap<String,Object>()
      objects[className] = objectsForClass
    }
    objectsForClass[key] = obj
  }

  def load(String className, String key) {
    if (className==null) {
      return null
    } else {
      if (key==null) {
        // create object from class
        Class mappedClass = getMappedClass(className);
        if (mappedClass) {
          return mappedClass.newInstance()
        }
        return null
      } else {
        def objectsForClass = objects[className]
        if (objectsForClass==null) {
          return null
        }
        return objectsForClass[key]
      }
    }
  }

  def save(obj) {
    assert obj
    addObject obj
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

  def String getClassMapping(Class<?> clazz) {
    return clazz.getName()
  }

  def Class<?> getMappedClass(String className) {
    if (className==null) {
      return null
    }
    try {
      return Class.forName(className)
    } catch(ClassNotFoundException e) {
      return null
    }
  }

  def delete(obj) {
    assert obj
    String key = getKey(obj)
    assert key
    String className = getClassMapping(obj.getClass())
    assert className
    def objectsForClass = objects[className]
    if (objectsForClass) {
      return objectsForClass.remove(key)
    }
    return null
  }


}
