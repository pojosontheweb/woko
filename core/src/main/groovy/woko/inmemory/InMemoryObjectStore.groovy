package woko.inmemory

import woko.persistence.ObjectStore
import woko.persistence.ResultIterator

class InMemoryObjectStore implements ObjectStore {
  
  def objects = new HashMap<String,Map<String,Object>>()

  public void addObject(def obj) {
    assert obj != null
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
    assert obj != null
    addObject obj
  }

  String getKey(obj) {
    assert obj != null
    try {
      def id = obj._id
      if (id==null) {
        return null
      }
      return id.toString()
    } catch(MissingPropertyException e) {
      return null
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
    assert obj != null
    String key = getKey(obj)
    assert key != null
    String className = getClassMapping(obj.getClass())
    assert className
    def objectsForClass = objects[className]
    if (objectsForClass) {
      return objectsForClass.remove(key)
    }
    return null
  }

  ResultIterator list(String className, Integer start, Integer limit) {
    throw new UnsupportedOperationException()
  }

  List getMappedClasses() {
    def res = []
    objects.each {k,v ->
      res << Class.forName(k)
    }
    return res
  }

  def ResultIterator search(Object query, Integer start, Integer limit) {
    throw new UnsupportedOperationException("search is not implemented for the in-memory store")
  }


}
