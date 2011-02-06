package woko.users

public interface UserManager {

  List<String> getRoles(String username)

  boolean checkPassword(String username, String clearPassword)

}