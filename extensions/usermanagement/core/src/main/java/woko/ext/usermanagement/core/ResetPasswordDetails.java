package woko.ext.usermanagement.core;

import java.util.Date;

public interface ResetPasswordDetails {
    String getKey();
    String getEmail();
    Date getCreationDate();
}
