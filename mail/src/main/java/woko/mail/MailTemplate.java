package woko.mail;

import woko.Woko;

import java.util.Locale;
import java.util.Map;

public interface MailTemplate {

    String processSubject(Woko woko, Locale locale, Map<String,Object> binding);

    String processBody(Woko woko, Locale locale, Map<String,Object> binding);

}
