package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.*;

import static woko.ext.usermanagement.mail.BindingHelper.*;

public class MailTemplateRegister extends BaseMailTemplate {

    public static final String REGISTER_URL = "registerUrl";
    public static final String TEMPLATE_NAME = "register";

    public MailTemplateRegister() {
        super("woko.ext.usermanagement.register.mail.subject","woko.ext.usermanagement.register.mail.content");
    }


    @Override
    protected List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding) {
        String registerUrl = getBindingValueSafe(binding, REGISTER_URL);
        List<String> args = new ArrayList<String>(super.getArgsBody(woko, locale, binding));
        args.add(registerUrl);
        return args;
    }
}
