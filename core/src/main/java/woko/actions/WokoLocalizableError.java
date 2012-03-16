/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
