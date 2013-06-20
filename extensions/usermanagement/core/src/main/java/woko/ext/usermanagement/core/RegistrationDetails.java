package woko.ext.usermanagement.core;

public interface RegistrationDetails<U extends User> {

    String getKey();

    String getSecretToken();

    U getUser();

    /**
     * To be invoked when the registration has been activated. Denormalizes user
     * fields and cuts the relation between reg details and User.
     * @return <code>true</code> if the user could be activated (getUser()!=null), <code>false</code> otherwise
     */
    boolean activate();


}
