package woko.ext.usermanagement.core;

import woko.users.UserManager;

import java.util.List;

public interface RegistrationAwareUserManager<U extends User> extends UserManager{

    RegistrationDetails<U> createRegistration(U user);

    AccountStatus getRegisteredAccountStatus();

    List<String> getRegisteredRoles();
}
