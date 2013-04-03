package woko.ext.usermanagement.util;

public class PasswordUtil {

    public static boolean validatePasswords(String password1, String password2) {
        // TODO more validation (length, strength etc.)
        boolean valid = true;
        if (password1==null) {
            valid = false;
        }
        if (valid && password2==null) {
            valid = false;
        }
        if (valid) {
            valid = password1.equals(password2);
        }
        return valid;
    }

}
