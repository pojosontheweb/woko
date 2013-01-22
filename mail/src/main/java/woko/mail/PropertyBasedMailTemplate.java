package woko.mail;

import woko.Woko;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class PropertyBasedMailTemplate implements MailTemplate {

    private final String keySubject;
    private final String keyBody;

    public PropertyBasedMailTemplate(String keySubject, String keyBody) {
        this.keySubject = keySubject;
        this.keyBody = keyBody;
    }

    @Override
    public String processSubject(Woko woko, Locale locale, Map<String, Object> binding) {
        return woko.getLocalizedMessage(
                locale,
                keySubject,
                listToArray(getArgsSubject(woko, locale, binding))
        );
    }

    protected abstract List<String> getArgsSubject(Woko woko, Locale locale, Map<String, Object> binding);

    @Override
    public String processBody(Woko woko, Locale locale, Map<String, Object> binding) {
        return woko.getLocalizedMessage(
                locale,
                keyBody,
                listToArray(getArgsBody(woko, locale, binding))
        );
    }

    protected String[] listToArray(List<String> argsBody) {
        String[] res = new String[argsBody.size()];
        return argsBody.toArray(res);
    }

    protected abstract List<String> getArgsBody(Woko woko, Locale locale, Map<String, Object> binding);

}
