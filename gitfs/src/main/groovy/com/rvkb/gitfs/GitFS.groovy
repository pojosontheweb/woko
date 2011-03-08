package com.rvkb.gitfs

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class GitFS {

  private final Repository repository

  private void debug(def msg) {
    println "[GitFS] $msg"
  }

  GitFS(File pathToRepo) {
    assert pathToRepo != null
    debug("Initializing with pathToRepo ${pathToRepo.absolutePath}")
    repository = new FileRepositoryBuilder().
        setGitDir(pathToRepo).
        build()
  }

  Session openSession(UserInfo userInfo) {
    return new Session(repository, userInfo)

  }

  def doInSession(UserInfo userInfo, Closure c) {
    Session session = openSession(userInfo)
    return c.call(session)
  }

}
