package woko.ext.usermanagement.mail;

public class MailTemplatePassword extends BaseMailTemplate {

    public static final String TEMPLATE_NAME = "password";

    public MailTemplatePassword() {
        super("woko.ext.usermanagement.password.mail.subject","woko.ext.usermanagement.password.mail.content");
    }

}
