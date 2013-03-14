package woko.ext.usermanagement.mail;

public class MailTemplateAccountActivated extends BaseMailTemplate {

    public static final String TEMPLATE_NAME = "accountActivated";

    public MailTemplateAccountActivated() {
        super("woko.ext.usermanagement.account.activated.email.subject","woko.ext.usermanagement.account.activated.email.content");
    }

}
