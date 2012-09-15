package woko.ext.usermanagement.core;

public interface RegistrationAwareUserManager<U extends User> {

    RegistrationDetails<U> getRegistrationDetail(String key);

    RegistrationDetails<U> createRegistration(U user);


}
