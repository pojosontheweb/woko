package woko.mail;

import woko.Woko;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class SmtpMailService extends MailServiceBase {

    private final String smtpHost;
    private final int smtpPort;
    private final Boolean smtpAuth;

    private String encoding = "utf-8";

    private String smtpAuthUsername;
    private String smtpAuthPassword;

    public SmtpMailService(String appUrl,
                           String fromEmailAddress,
                           Map<String, MailTemplate> templates,
                           String smtpHost,
                           int smtpPort,
                           Boolean smtpAuth) {
        super(appUrl, fromEmailAddress, templates);
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpAuth = smtpAuth;
    }

    public SmtpMailService setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String, Object> binding) {

        String from = getFromEmailAddress();
        Properties props = new Properties();

        // Setup mail server
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", smtpAuth);

        Session session;
        if (smtpAuth) {
            session = Session.getDefaultInstance(props, createAuthenticator());
        } else {
            session = Session.getDefaultInstance(props);
        }

        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(template.processSubject(woko, locale, binding), encoding);
            message.setContent(template.processBody(woko, locale, binding),
                    "text/html; charset=" + encoding);
            message.setSentDate(new Date());
            Transport.send(message);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Authenticator createAuthenticator() {
        return new SmtpAuthenticator();
    }

    private class SmtpAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpAuthUsername, smtpAuthPassword);
        }
    }

}
