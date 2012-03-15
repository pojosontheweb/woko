package woko.actions;

import net.sourceforge.stripes.localization.LocalizationUtility;
import net.sourceforge.stripes.validation.LocalizableError;
import woko.util.WLogger;

import java.util.Locale;

public class WokoLocalizableError extends LocalizableError {

    private static final WLogger logger = new WLogger(WokoLocalizableError.class);

    private String fieldKey;
    
    public WokoLocalizableError(String messageKey, Object... parameter) {
        this(messageKey, null, parameter);
    }
    public WokoLocalizableError(String messageKey, String fieldKey, Object... parameter) {
        super(messageKey, parameter);
        this.fieldKey = fieldKey;
    }

    protected void resolveFieldName(Locale locale) {
        logger.debug("Replace fieldName (object.) with its className : MyClass.myProp");

        if (fieldKey == null) {
            getReplacementParameters()[0] = "FIELD NAME NOT SUPPLIED IN CODE";
        }
        else {
            getReplacementParameters()[0] =
                    LocalizationUtility.getLocalizedFieldName(fieldKey,
                            getActionPath(),
                            getBeanclass(),
                            locale);

            if (getReplacementParameters()[0] == null) {
                getReplacementParameters()[0] =
                        LocalizationUtility.makePseudoFriendlyName(fieldKey);
            }
        }
    }
}
