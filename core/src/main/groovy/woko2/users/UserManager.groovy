package woko2.users

public interface UserManager {

  User getUser(String username)

  List<String> getRoles(String username)

  boolean checkPassword(String username, String clearPassword)

}