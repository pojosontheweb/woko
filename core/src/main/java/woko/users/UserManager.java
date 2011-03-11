package woko.users;

import java.util.List;

public interface UserManager {

  List<String> getRoles(String username);

  boolean checkPassword(String username, String password);

}