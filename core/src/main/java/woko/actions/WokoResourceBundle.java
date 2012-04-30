/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

