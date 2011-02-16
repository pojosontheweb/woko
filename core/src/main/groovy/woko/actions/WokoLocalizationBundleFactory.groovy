package woko.actions

import net.sourceforge.stripes.localization.LocalizationBundleFactory
import net.sourceforge.stripes.config.Configuration

class WokoLocalizationBundleFactory implements LocalizationBundleFactory {

    public ResourceBundle getFormFieldBundle(Locale locale) {
        return new WokoResourceBundle(locale)
    }

    public ResourceBundle getErrorMessageBundle(Locale locale) {
        return new WokoResourceBundle(locale)
    }

    public void init(Configuration configuration) { }
}
