package woko.mail;

public class ConsoleMailService implements MailService {

    private final String appUrl;
    private final String fromEmailAddress;

    public ConsoleMailService(String appUrl, String fromEmailAddress) {
        this.appUrl = appUrl;
        this.fromEmailAddress = fromEmailAddress;
    }

    @Override
    public void sendMail(String to, String text) {
        System.out.println("FAKE Sending email from " + getFromEmailAddress() + " to " +
            to + ", text :\n" + text);
    }

    @Override
    public String getAppUrl() {
        return appUrl;
    }

    @Override
    public String getFromEmailAddress() {
        return fromEmailAddress;
    }
}
