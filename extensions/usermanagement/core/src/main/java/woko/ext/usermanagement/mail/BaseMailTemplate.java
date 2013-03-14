package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.mail.PropertyBasedMailTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static woko.ext.usermanagement.mail.BindingHelper.getAppName;
import static woko.ext.usermanagement.mail.BindingHelper.getAppUrl;
import static woko.ext.usermanagement.mail.BindingHelper.getUsername;

public abstract class BaseMailTemplate extends PropertyBasedMailTemplate {

    public BaseMailTemplate(String keySubject, String keyBody) {
        super(keySubject, keyBody);
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
