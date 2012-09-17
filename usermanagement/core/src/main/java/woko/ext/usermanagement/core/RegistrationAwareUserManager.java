package woko.ext.usermanagement.core;

import java.util.List;

public interface RegistrationAwareUserManager<U extends User> {

    RegistrationDetails<U> getRegistrationDetail(String key);

    RegistrationDetails<U> createRegistration(U user);

    void save(U user);

    List<String> getRegisteredUserRoles();

    AccountStatus getRegisteredAccountStatus();
}
