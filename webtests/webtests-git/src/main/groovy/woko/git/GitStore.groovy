package woko.git

import woko.persistence.ObjectStore
import woko.persistence.ResultIterator

class GitStore implements ObjectStore {
  Object load(String className, String key) {
    // load the file and demarshal object using converter
    Class<? extends GitEntityConverter> converterClass = entityClass.getAnnotation(GitEntity.class)?.converter()
    assert converterClass!=null
    GitEntityConverter converter = converterClass.newInstance()
    def result = null
    gfs.doInSession(userInfo) { Session s ->
      s.readFile(new File(s.getAbsolutePath(relativePath))) { InputStream is ->
        result = converter.fromStream(is)
      }
    }
    return result
  }

  Object save(Object obj) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  Object delete(Object obj) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  String getKey(Object obj) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  String getClassMapping(Class<?> clazz) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  Class<?> getMappedClass(String className) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  ResultIterator list(String className, Integer start, Integer limit) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  List<Class<?>> getMappedClasses() {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  ResultIterator search(Object query, Integer start, Integer limit) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }


}
