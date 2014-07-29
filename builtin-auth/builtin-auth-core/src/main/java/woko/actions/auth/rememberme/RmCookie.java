package woko.actions.auth.rememberme;

public class RmCookie {

    private final String username;
    private final String series;
    private final String token;

    public RmCookie(String username, String series, String token) {
        this.username = username;
        this.series = series;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getSeries() {
        return series;
    }

    public String getToken() {
        return token;
    }

    public String toPath() {
        return username + RememberMeInterceptor.COOKIE_VAL_SEPARATOR +
                series + RememberMeInterceptor.COOKIE_VAL_SEPARATOR + token;
    }
}
