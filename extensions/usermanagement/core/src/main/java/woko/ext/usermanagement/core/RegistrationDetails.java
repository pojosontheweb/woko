package woko.ext.usermanagement.core;

import java.util.Date;

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

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link RegistrationDetails#activate()}
     */
    Date getCreatedOn();

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link RegistrationDetails#activate()}
     */
    Date getActivatedOn();

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link RegistrationDetails#activate()}
     */
    Long getActivatedUserId();

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link RegistrationDetails#activate()}
     */
    String getActivatedUsername();

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link RegistrationDetails#activate()}
     */
    String getActivatedEmail();

}
