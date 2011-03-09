package com.rvkb.gitfs

class GitObjectStore {

  private final GitFS gfs

  GitObjectStore(GitFS gfs) {
    this.gfs = gfs
  }

  def load(UserInfo userInfo, Class<?> entityClass, String relativePath) {
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

  def save(UserInfo userInfo, Object obj, String commitMessage) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  def delete(UserInfo userInfo, Object obj, String commitMessage) {
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

}
