package woko.actions;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class WokoResourceBundle extends ResourceBundle {

    private static final String WOKO_RESOURCES_BUNDLE = "WokoResources";
    private static final String APP_RESOURCES_BUNDLE = "ApplicationResources";

    private Locale locale;

    public WokoResourceBundle(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String fullKey) {
        // First, try to get resources from app specific bundle
        String result = getResult(locale, APP_RESOURCES_BUNDLE, fullKey);
        if (result == null)
            result = getResult(locale, WOKO_RESOURCES_BUNDLE, fullKey);

        return result;

    }

    // Just returns null if the bundle or the key is not found,
    // instead of throwing an exception.
    private String getResult(Locale loc, String name, String key) {
        String result = null;
        ResourceBundle bundle = ResourceBundle.getBundle(name, loc);
        if (bundle != null) {
            try { result = bundle.getString(key); }
            catch (MissingResourceException exc) { }
        }
        return result;
    }
}

