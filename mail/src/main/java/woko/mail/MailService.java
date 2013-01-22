package woko.mail;

import woko.Woko;

import java.util.Locale;
import java.util.Map;

public interface MailService {

    public static final String KEY = "MailService";

    /**
     * Sends the email to passed recipient using specified template
     * and binding.
     * @param woko the Woko instance
     * @param to the recipient's email address
     * @param locale the <code>Locale</code> to be used
     * @param template the <code>MailTemplate</code> to be used
     * @param binding the binding to be used
     */
    void sendMail(Woko woko, String to, Locale locale, MailTemplate template, Map<String,Object> binding);

    /**
     * Return the URL of the application to be used in email links.
     * @return the app URL
     */
    String getAppUrl();

    /**
     * Return the sender's email address to be used in emails
     * @return the "from" email address
     */
    String getFromEmailAddress();

    /**
     * Return a <code>MailTemplate</code> for passed name
     * @param name the name of the MailTemplate
     * @return a <code>MailTemplate</code>
     */
    MailTemplate getMailTemplate(String name);

}
