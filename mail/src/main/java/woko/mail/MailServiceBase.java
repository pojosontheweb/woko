package woko.mail;

import java.util.HashMap;
import java.util.Map;

public abstract class MailServiceBase implements MailService {

    private final String appUrl;
    private final String fromEmailAddress;
    private final Map<String,MailTemplate> templates;

    public MailServiceBase(String appUrl, String fromEmailAddress, Map<String, MailTemplate> templates) {
        this.appUrl = appUrl;
        this.fromEmailAddress = fromEmailAddress;
        this.templates = new HashMap<String, MailTemplate>(templates);
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
