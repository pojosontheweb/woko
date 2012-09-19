package woko.mail;

public class ConsoleMailService implements MailService {

    @Override
    public void sendMail(String from, String to, String text) {
        System.out.println("FAKE Sending email from " + from + " to " +
            to + ", text :\n" + text);
    }

}
