package woko.ext.usermanagement.core;

import woko.users.UserManager;

import java.util.List;

public interface RegistrationAwareUserManager<U extends User> extends UserManager{

    RegistrationDetails<U> getRegistrationDetail(String key);

    RegistrationDetails<U> createRegistration(U user);

}
