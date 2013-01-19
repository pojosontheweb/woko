package woko.mail;

import woko.Woko;

import javax.mail.*;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
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

    public SmtpMailService setSmtpAuthUsername(String smtpAuthUsername) {
        this.smtpAuthUsername = smtpAuthUsername;
        return this;
    }

    public SmtpMailService setSmtpAuthPassword(String smtpAuthPassword) {
        this.smtpAuthPassword = smtpAuthPassword;
        return this;
    }

    @Override
    public void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String, Object> binding) {

        String from = getFromEmailAddress();
        Properties props = new Properties();

        // Setup mail server
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", Integer.toString(smtpPort));
        props.put("mail.smtp.auth", smtpAuth.toString());
//        props.put("mail.smtp.timeout","30000");
        props.put("mail.smtp.starttls.enable","true");

        try {
            Session session = smtpAuth ?
                Session.getDefaultInstance(props, createAuthenticator()) :
                Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(template.processSubject(woko, locale, binding), encoding);
            message.setContent(template.processBody(woko, locale, binding),
                    "text/html; charset=" + encoding);
            message.setSentDate(new Date());

            Transport t = session.getTransport();
            t.addTransportListener(new TransportListener() {
                @Override
                public void messageDelivered(TransportEvent e) {
                    System.out.println("messageDelivered:" + e);
                }

                @Override
                public void messageNotDelivered(TransportEvent e) {
                    System.out.println("messageNotDelivered:" + e);
                }

                @Override
                public void messagePartiallyDelivered(TransportEvent e) {
                    System.out.println("messagePartiallyDelivered:" + e);
                }
            });
            t.connect();
            try {
                t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            } finally {
                t.close();
            }
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
