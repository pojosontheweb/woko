package woko.ext.usermanagement.core;

public interface RegistrationDetails<U extends User> {

    String getKey();

    String getSecretToken();

    U getUser();

}
