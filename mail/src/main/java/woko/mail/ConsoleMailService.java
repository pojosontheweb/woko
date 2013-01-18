package woko.mail;

import woko.Woko;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConsoleMailService implements MailService {

    private final String appUrl;
    private final String fromEmailAddress;
    private final Map<String,MailTemplate> templates;

    public ConsoleMailService(String appUrl, String fromEmailAddress, Map<String, MailTemplate> templates) {
        this.appUrl = appUrl;
        this.fromEmailAddress = fromEmailAddress;
        this.templates = new HashMap<String, MailTemplate>(templates);
    }

    @Override
    public void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String, Object> binding) {
        System.out.println("FAKE Sending email from " + getFromEmailAddress() +
                " to " + to +
                ",subject : " + template.processSubject(woko, locale, binding) +
                "\n, text :\n" + template.processBody(woko, locale, binding));
    }

    @Override
    public MailTemplate getMailTemplate(String name) {
        return templates.get(name);
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


