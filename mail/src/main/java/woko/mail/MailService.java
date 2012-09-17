package woko.mail;

public interface MailService {

    public static final String KEY = "MailService";

    void sendMail(String from, String to, String text);

}
