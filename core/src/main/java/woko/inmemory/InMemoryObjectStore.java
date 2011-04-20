package woko.inmemory;

import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryObjectStore implements ObjectStore {

  Map<String,Map<String,Object>> objects = new HashMap<String,Map<String,Object>>();

  @Override
  public void close() {
  }

  public void addObject(String key, Object obj) {
    Util.assertArg("obj", obj);
    String className = getClassMapping(obj.getClass());
    Map<String,Object> objectsForClass = objects.get(className);
    if (objectsForClass==null) {
      objectsForClass = new HashMap<String,Object>();
      objects.put(className, objectsForClass);
    }
    objectsForClass.put(key, obj);
  }

  public Object load(String className, String key) {
    if (className==null) {
      return null;
    } else {
      if (key==null) {
        // create object from class
        Class mappedClass = getMappedClass(className);
        if (mappedClass!=null) {
          try {
            return mappedClass.newInstance();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
        return null;
      } else {
        Map<String,Object> objectsForClass = objects.get(className);
        if (objectsForClass==null) {
          return null;
        }
        return objectsForClass.get(key);
      }
    }
  }

  public Object save(Object obj) {
    Util.assertArg("obj", obj);
    addObject(getKey(obj), obj);
    return obj;
  }

  public String getKey(Object obj) {
    Util.assertArg("obj", obj);
    for (String className : objects.keySet()) {
      Map<String,Object> objectsForClass = objects.get(className);
      for (String key : objectsForClass.keySet()) {
        Object value = objectsForClass.get(key);
        if (value!=null && value.equals(obj)) {
          return key;
        }
      }
    }
    return null;
  }

  public String getClassMapping(Class<?> clazz) {
    Util.assertArg("clazz", clazz);
    return clazz.getName();
  }

  public Class<?> getMappedClass(String className) {
    if (className==null) {
      return null;
    }
    try {
      return Class.forName(className);
    } catch(ClassNotFoundException e) {
      return null;
    }
  }

  public Object delete(Object obj) {
    Util.assertArg("obj", obj);
    String key = getKey(obj);
    if (key!=null) {
      String className = getClassMapping(obj.getClass());
      if (className!=null) {
        Map<String,Object> objectsForClass = objects.get(className);
        if (objectsForClass!=null) {
          return objectsForClass.remove(key);
        }

      }
    }
    return null;
  }

  public ResultIterator list(String className, Integer start, Integer limit) {
    throw new UnsupportedOperationException();
  }

  public List<Class<?>> getMappedClasses() {
    List<Class<?>> res = new ArrayList<Class<?>>();
    for (String k : objects.keySet()) {
      try {
        res.add(Class.forName(k));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return res;
  }

  public ResultIterator search(Object query, Integer start, Integer limit) {
    throw new UnsupportedOperationException("search is not implemented for the in-memory store");
  }


}
