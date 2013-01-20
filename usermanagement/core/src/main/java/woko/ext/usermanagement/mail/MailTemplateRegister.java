package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static woko.ext.usermanagement.mail.BindingHelper.*;

public class MailTemplateRegister extends PropertyBasedMailTemplate {

    public static final String REGISTER_URL = "registerUrl";
    public static final String TEMPLATE_NAME = "register";

    public MailTemplateRegister() {
        super("woko.ext.usermanagement.register.mail.subject","woko.ext.usermanagement.register.mail.content");
    }

    @Override
    protected List<String> getArgsSubject(Woko woko, Locale locale, Map<String, Object> binding) {
        return Arrays.asList(getAppName(binding));
    }

    @Override
    protected List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding) {
        String registerUrl = getBindingValueSafe(binding, REGISTER_URL);
        return Arrays.asList(
                getUsername(binding),
                getAppName(binding),
                getAppUrl(binding),
                registerUrl);
    }
}
