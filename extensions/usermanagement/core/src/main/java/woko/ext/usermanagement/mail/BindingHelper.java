package woko.ext.usermanagement.mail;

import woko.Woko;
import woko.ext.usermanagement.core.User;
import woko.mail.MailService;
import woko.mail.MailTemplate;

import java.util.HashMap;
import java.util.Map;

public final class BindingHelper {

    public static final String APP_NAME = "appName";
    public static final String USERNAME = "username";
    public static final String APP_URL = "appUrl";
    public static final String WOKO = "woko";
    public static final String USER = "user";

    @SuppressWarnings("unckeched")
    public static <T> T getBindingValueSafe(Map<String, Object> binding, String key) {
        T obj = (T)binding.get(key);
        if (obj==null) {
            throw new IllegalArgumentException(key + " not found in binding " + binding);
        }
        return obj;
    }

    public static String getAppName(Map<String, Object> binding) {
        return getBindingValueSafe(binding, APP_NAME);
    }

    public static String getUsername(Map<String, Object> binding) {
        return getBindingValueSafe(binding, USERNAME);
    }

    public static String getAppUrl(Map<String, Object> binding) {
        return getBindingValueSafe(binding, APP_URL);
    }

    public static <T extends User> Map<String,Object> newBinding(Woko<?,?,?,?> woko, T user, String appName, MailService mailService) {
        Map<String,Object> res = new HashMap<String, Object>();
        res.put(WOKO, woko);
        res.put(USER, user);
        res.put(USERNAME, user.getUsername());
        res.put(APP_NAME, appName);
        res.put(APP_URL, mailService.getAppUrl());
        return res;
    }

    public static Map<String,MailTemplate> createDefaultMailTemplates() {
        Map<String,MailTemplate> res = new HashMap<String, MailTemplate>();
        res.put(MailTemplateRegister.TEMPLATE_NAME, new MailTemplateRegister());
        res.put(MailTemplatePassword.TEMPLATE_NAME, new MailTemplatePassword());
        res.put(MailTemplateResetPassword.TEMPLATE_NAME, new MailTemplateResetPassword());
        res.put(MailTemplateAccountActivated.TEMPLATE_NAME, new MailTemplateAccountActivated());
        return res;
    }
}
