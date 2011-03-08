package com.rvkb.gitfs

class UserInfo {

  private final String username
  private final String email

  UserInfo(String username, String email) {
    this.username = username
    this.email = email
  }

  String getUsername() {
    return username
  }

  String getEmail() {
    return email
  }

  public String toString ( ) {
    return "UserInfo{" +
    "username='" + username + '\'' +
    ", email='" + email + '\'' +
    '}' ;
  }
}
