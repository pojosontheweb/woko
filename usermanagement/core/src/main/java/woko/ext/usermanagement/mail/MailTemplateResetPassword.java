package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.*;

import static woko.ext.usermanagement.mail.BindingHelper.*;

public class MailTemplateResetPassword extends BaseMailTemplate {

    public static final String RESET_PASSWORD_URL = "resetUrl";
    public static final String TEMPLATE_NAME = "resetPassword";

    public MailTemplateResetPassword() {
        super("woko.ext.usermanagement.password.reset.email.subject","woko.ext.usermanagement.password.reset.email.content");
    }

    @Override
    protected List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding) {
        String resetUrl = getBindingValueSafe(binding, RESET_PASSWORD_URL);
        List<String> args = new ArrayList<String>(super.getArgsBody(woko, locale, binding));
        args.add(resetUrl);
        return args;
    }
}
