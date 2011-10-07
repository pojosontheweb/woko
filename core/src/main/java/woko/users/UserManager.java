package woko.users;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserManager {

  List<String> getRoles(String username);

  boolean authenticate(String username, HttpServletRequest request);

}