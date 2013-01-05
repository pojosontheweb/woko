package woko.mail;

public interface MailService {

    public static final String KEY = "MailService";

    void sendMail(String to, String subject, String text);

    String getAppUrl();

    String getFromEmailAddress();

}
