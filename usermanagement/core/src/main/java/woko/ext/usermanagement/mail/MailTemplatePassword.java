package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static woko.ext.usermanagement.mail.BindingHelper.*;

public class MailTemplatePassword extends PropertyBasedMailTemplate {

    public static final String TEMPLATE_NAME = "password";

    public MailTemplatePassword() {
        super("woko.ext.usermanagement.password.mail.subject","woko.ext.usermanagement.password.mail.content");
    }

    @Override
    protected List<String> getArgsSubject(Woko woko, Locale locale, Map<String, Object> binding) {
        return Arrays.asList(getAppName(binding));
    }

    @Override
    protected List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding) {
        return Arrays.asList(
                getUsername(binding),
                getAppName(binding),
                getAppUrl(binding));
    }
}
