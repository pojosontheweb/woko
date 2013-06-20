package woko.ext.usermanagement.hibernate;

import woko.ext.usermanagement.core.RegistrationDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class HbRegistrationDetails<U extends HbUser> implements RegistrationDetails<U> {

    @Id
    @Column(name = "registration_key")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    private HbUser user;

    @NotNull
    private String secretToken;

    private Date createdOn = new Date();

    private Date activatedOn;

    private Long activatedUserId;

    private String activatedUserName;

    private String activatedEmail;

    @Override
    public boolean activate() {
        if (user!=null) {
            activatedOn = new Date();
            activatedUserId = user.getId();
            activatedUserName = user.getUsername();
            activatedEmail = user.getEmail();
            user = null;
            return true;
        }
        return false;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public U getUser() {
        return (U)user;
    }

    public void setUser(U user) {
        this.user = user;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getActivatedOn() {
        return activatedOn;
    }

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link woko.ext.usermanagement.hibernate.HbRegistrationDetails#activate()}
     */
    public Long getActivatedUserId() {
        return activatedUserId;
    }

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link woko.ext.usermanagement.hibernate.HbRegistrationDetails#activate()}
     */
    public String getActivatedUserName() {
        return activatedUserName;
    }

    /**
     * Set when the registration is activated, no setter.
     * Allows to keep track of the User info at registration
     * time without holding a reference to it.
     * @see {@link woko.ext.usermanagement.hibernate.HbRegistrationDetails#activate()}
     */
    public String getActivatedEmail() {
        return activatedEmail;
    }

    @Override
    public String toString() {
        return "HbRegistrationDetails{" +
                "key='" + key + '\'' +
                ", user=" + user +
                ", secretToken='" + secretToken + '\'' +
                ", createdOn=" + createdOn +
                ", activatedOn=" + activatedOn +
                ", activatedUserId=" + activatedUserId +
                ", activatedUserName='" + activatedUserName + '\'' +
                ", activatedEmail='" + activatedEmail + '\'' +
                '}';
    }
}
