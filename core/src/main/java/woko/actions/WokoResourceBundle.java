package woko.actions;

import net.sourceforge.stripes.localization.DefaultLocalizationBundleFactory;

import java.util.*;

public class WokoResourceBundle extends ResourceBundle {

    private Locale locale;
    private List<String> bundleNames;

    public WokoResourceBundle(Locale locale, List<String> bundleNames) {
        this.locale = locale;
        this.bundleNames = bundleNames;
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result = null;
        if (bundleNames != null) {
            // Look in each configured bundle
            for (String bundleName : bundleNames) {
                if (bundleName != null) {
                    result = getFromBundle(locale, bundleName, key);
                    if (result != null) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    // Just returns null if the bundle or the key is not found,
    // instead of throwing an exception.
    private String getFromBundle(Locale loc, String name, String key) {
        String result = null;
        ResourceBundle bundle = ResourceBundle.getBundle(name, loc);
        if (bundle != null) {
            try { result = bundle.getString(key); }
            catch (MissingResourceException exc) { }
        }
        return result;
    }
}

