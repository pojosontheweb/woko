package woko.mail;

import woko.Woko;
import woko.util.WLogger;

import javax.mail.*;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.*;

public class SmtpMailService extends MailServiceBase {

    private static final WLogger logger = WLogger.getLogger(SmtpMailService.class);

    private String encoding = "utf-8";

    private final List<TransportListener> transportListeners = new ArrayList<TransportListener>();

    public SmtpMailService(String appUrl,
                           String fromEmailAddress,
                           Map<String, MailTemplate> templates) {
        super(appUrl, fromEmailAddress, templates);
        logger.info("Created with appUrl=" + appUrl +", fromEmailAddress=" + fromEmailAddress + ", " + templates.size() + " templates");
    }

    public SmtpMailService setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public SmtpMailService addTransportListener(TransportListener l) {
        transportListeners.add(l);
        return this;
    }

    public SmtpMailService removeTransportListener(TransportListener l) {
        transportListeners.remove(l);
        return this;
    }

    @Override
    public void sendMail(Woko woko, final String to, final Locale locale, final MailTemplate template, final Map<String, Object> binding) {

        logger.debug("Sending email to " + to + " using template " + template + " and locale " + locale + ", binding=" + binding);

        String from = getFromEmailAddress();
        Properties props = getMailSessionProperties();

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
            // add our logging listener
            t.addTransportListener(new TransportListener() {
                @Override
                public void messageDelivered(TransportEvent e) {
                    logger.debug("Email delivered to " + to + " using template " + template + " and locale " + locale + ", binding=" + binding);
                }

                @Override
                public void messageNotDelivered(TransportEvent e) {
                    logger.error("Email NOT delivered to " + to + " using template " + template + " and locale " + locale + ", binding=" + binding + ", transportEvent=" + e);
                }

                @Override
                public void messagePartiallyDelivered(TransportEvent e) {
                    logger.error("Email PARTIALLY delivered to " + to + " using template " + template + " and locale " + locale + ", binding=" + binding + ", transportEvent=" + e);
                }
            });
            // add user supplied listeners
            for (TransportListener l : transportListeners) {
                t.addTransportListener(l);
            }

            t.connect();
            try {
                t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            } finally {
                t.close();
            }
        } catch(Exception e) {
            logger.error("Caught exception while sending email" + to + " using template " + template + " and locale " + locale + ", binding=" + binding, e);
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
            logger.info("Using authentication (username=" + username + ")");
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
            String msg = "Could not find property file " + PROPERTY_FILE + " in the CLASSPATH.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        try {
            properties.load(is);
            return properties;
        } catch (Exception e) {
            String msg = "Unable to load properties from " + PROPERTY_FILE;
            logger.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}
