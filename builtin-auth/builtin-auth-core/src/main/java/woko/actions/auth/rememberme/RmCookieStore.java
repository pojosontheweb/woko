package woko.actions.auth.rememberme;

public interface RmCookieStore {

    static final String KEY = RmCookieStore.class.getSimpleName();

    RmCookie getCookie(String username, String series);

    RmCookie updateToken(RmCookie cookie);

    void deleteAllForUser(String username);

    RmCookie createCookie(String user);
}
