package com.rvkb.gitfs

class Revision {

  private final String message
  private final UserInfo userInfo
  private final String id

  Revision(String message, UserInfo userInfo, String id) {
    this.message = message
    this.userInfo = userInfo
    this.id = id
  }

  String getMessage() {
    return message
  }

  String getId() {
    return id
  }

  UserInfo getUserInfo() {
    return userInfo
  }

  public String toString ( ) {
    return "Revision{" +
    "message='" + message + '\'' +
    ", userInfo=" + userInfo +
    ", id='" + id + '\'' +
    '}' ;
  }

}
