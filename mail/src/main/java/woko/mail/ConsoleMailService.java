package woko.mail;

import woko.Woko;

import java.util.Locale;
import java.util.Map;

public class ConsoleMailService extends MailServiceBase {

    public ConsoleMailService(String appUrl, String fromEmailAddress, Map<String, MailTemplate> templates) {
        super(appUrl, fromEmailAddress, templates);
    }

    @Override
    public void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String, Object> binding) {
        System.out.println("FAKE Sending email from " + getFromEmailAddress() +
                " to " + to +
                ",subject : " + template.processSubject(woko, locale, binding) +
                "\n, text :\n" + template.processBody(woko, locale, binding));
    }
}


