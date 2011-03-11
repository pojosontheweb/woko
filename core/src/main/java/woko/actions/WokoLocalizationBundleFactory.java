package woko.actions;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.LocalizationBundleFactory;

import java.util.Locale;
import java.util.ResourceBundle;

public class WokoLocalizationBundleFactory implements LocalizationBundleFactory {

    public ResourceBundle getFormFieldBundle(Locale locale) {
        return new WokoResourceBundle(locale);
    }

    public ResourceBundle getErrorMessageBundle(Locale locale) {
        return new WokoResourceBundle(locale);
    }

    public void init(Configuration configuration) { }
}
