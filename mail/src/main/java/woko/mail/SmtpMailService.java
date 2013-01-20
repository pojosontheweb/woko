package woko.mail;

import woko.Woko;

import javax.mail.*;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class SmtpMailService extends MailServiceBase {

    private String encoding = "utf-8";

    public SmtpMailService(String appUrl,
                           String fromEmailAddress,
                           Map<String, MailTemplate> templates) {
        super(appUrl, fromEmailAddress, templates);
    }

    public SmtpMailService setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String, Object> binding) {

        String from = getFromEmailAddress();
        Properties props = getMailSessionProperties();

        // Setup mail server
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.host", smtpHost);
//        props.put("mail.smtp.port", Integer.toString(smtpPort));
//        props.put("mail.smtp.auth", smtpAuth.toString());
//        props.put("mail.smtp.timeout","30000");
//        props.put("mail.smtp.starttls.enable","true");

        String smtpAuthProp = props.getProperty("mail.smtp.auth", "false");
        boolean smtpAuth = Boolean.parseBoolean(smtpAuthProp);
        try {
            Session session = smtpAuth ?
                Session.getDefaultInstance(props, createAuthenticator(props)) :
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

    protected Authenticator createAuthenticator(Properties mailSessionProperties) {
        return new SmtpAuthenticator(
                mailSessionProperties.getProperty("mail.smtp.username"),
                mailSessionProperties.getProperty("mail.smtp.password"));
    }

    private class SmtpAuthenticator extends Authenticator {

        private final String username;
        private final String password;

        private SmtpAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    public static final String PROPERTY_FILE = "/woko.mail.properties";

    protected Properties getMailSessionProperties() {
        Properties properties = new Properties();
        InputStream is = SmtpMailService.class.getResourceAsStream(PROPERTY_FILE);
        if (is==null) {
            throw new IllegalStateException("Could not find property file " + PROPERTY_FILE + " in the CLASSPATH.");
        }
        try {
            properties.load(is);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Unable to load properties from " + PROPERTY_FILE, e);
        }
    }

}
