package com.rvkb.gitfs

import org.eclipse.jgit.revwalk.RevCommit

class WriteResult {

  private final RevCommit revCommit
  private final Exception exception

  WriteResult(RevCommit revCommit, Exception exception) {
    this.revCommit = revCommit
    this.exception = exception
  }

  String getRevision() {
    return revCommit.toObjectId().toString()
  }

  UserInfo getUserInfo() {
    def pi = revCommit.authorIdent
    return new UserInfo(pi.name, pi.emailAddress)
  }

  String getMessage() {
    return revCommit.fullMessage
  }

  Exception getException() {
    return exception
  }
}
