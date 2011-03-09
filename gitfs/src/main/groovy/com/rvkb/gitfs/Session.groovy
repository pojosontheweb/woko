package com.rvkb.gitfs

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.dircache.DirCache
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import org.eclipse.jgit.revwalk.RevCommitList
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.TreeFilter
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.AnyObjectId

class Session {

  private final Repository repository
  private final UserInfo userInfo
  private final File repoRootPath
  private final Git git

  private final Object lock = new Object()

  private void debug(def msg) {
    println "[Session] $msg"
  }

  Session(Repository repository, UserInfo userInfo) {
    assert repository
    assert userInfo
    this.repository = repository
    this.userInfo = userInfo
    repoRootPath = repository.workTree
    git = new Git(repository)
  }

  /**
   * Grabs the contents of file f as an InputStream, and
   * passes it to closure c
   * @param f the file to read
   * @param c the closure to execute (gets the InputStream as argument)
   * @return the return value of c
   */
  def readFile(File f, Closure c) {
    assert f
    assert c
    if (!f.exists()) {
      throw new IllegalArgumentException("File $f.absolutePath doesn't exist !")
    }
    String filePath = f.absolutePath
    if (!filePath.startsWith(repoRootPath.absolutePath)) {
      throw new IllegalArgumentException("File $f.absolutePath is not part of the repo !")
    }
    def result = null
    synchronized(lock) {
      f.withInputStream { is ->
        result = c.call(is)
      }
      return result
    }
  }

  /**
   * Write to file and commit.
   * @param is
   * @param f
   * @return
   */
  WriteResult writeToFile(InputStream is, File f, String message) {
    assert is
    // write to file and commit
    synchronized(lock) {
      debug("Writing stream to $f.absolutePath")
      f.withWriter { out ->
        out << new InputStreamReader(is)
      }
      debug("Commiting...")
      try {
        // add
        String fileName = f.absolutePath.substring(repoRootPath.absolutePath.length() + 1)
        git.add().addFilepattern(fileName).call()
        // and commit
        RevCommit rc = git.
            commit().
            setAll(true).
            setMessage(message).
            setAuthor(userInfo.username, userInfo.email).
            call()
        debug("... commited $rc")
        return new WriteResult(rc, null)
      } catch(Exception e) {
        debug("Caught exception : " + e)
        e.printStackTrace()
        return new WriteResult(null, e)
      }
    }
  }

  Iterable<Revision> getRevisions(File file, int max) {
    debug("Getting revisions for $file.absolutePath, max=$max")
    String fileName = getRelativePath(file)
    def revs = []
    synchronized(lock) {
      AnyObjectId headID = repository.resolve(Constants.HEAD)
      RevWalk walk = new RevWalk(repository)
      walk.markStart(walk.parseCommit(headID))
      walk.setTreeFilter(PathFilter.create(fileName))
      RevCommitList<RevCommit> list = new RevCommitList<RevCommit>()
      list.source(walk)
      list.fillTo(max)

      list.each { RevCommit rc ->
        revs << new Revision(
            rc.fullMessage,
            new UserInfo(rc.authorIdent.name, rc.authorIdent.emailAddress),
            rc.getId().toString())
      }
    }
    debug("returning ${revs.size()} revisions")
    return revs
  }

  File getRepoRootPath() {
    return repoRootPath
  }

  String getRelativePath(File file) {
    return file.absolutePath.substring(repoRootPath.absolutePath.length() + 1)
  }

  String getAbsolutePath(String relativePath) {
    return repoRootPath.absolutePath + File.separator + relativePath
  }

}