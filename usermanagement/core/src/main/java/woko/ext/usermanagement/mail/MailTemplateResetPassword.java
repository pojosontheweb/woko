package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static woko.ext.usermanagement.mail.BindingHelper.*;

public class MailTemplateResetPassword extends PropertyBasedMailTemplate {

    public static final String RESET_PASSWORD_URL = "resetUrl";
    public static final String TEMPLATE_NAME = "resetPassword";

    public MailTemplateResetPassword() {
        super("woko.ext.usermanagement.password.reset.email.subject","woko.ext.usermanagement.password.reset.email.content");
    }

    @Override
    protected List<String> getArgsSubject(Woko woko, Locale locale, Map<String, Object> binding) {
        return Arrays.asList(getAppName(binding));
    }

    @Override
    protected List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding) {
        String resetUrl = getBindingValueSafe(binding, RESET_PASSWORD_URL);
        return Arrays.asList(
                getUsername(binding),
                getAppName(binding),
                resetUrl,
                getAppUrl(binding));
    }
}
