package woko.ext.usermanagement.core;

import java.util.List;

public interface User {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String pasword);

    List<String> getRoles();

}
