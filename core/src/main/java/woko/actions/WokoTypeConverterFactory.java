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

import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;
import woko.Woko;
import woko.persistence.ObjectStore;

import java.util.Date;
import java.util.Locale;

public class WokoTypeConverterFactory extends DefaultTypeConverterFactory {

    public TypeConverter getTypeConverter(Class aClass, Locale locale) throws Exception {
        // check if the class is a Woko mapped class
        TypeConverter tc;
        ObjectStore store = Woko.getWoko(getConfiguration().getServletContext()).getObjectStore();
        if (store.getMappedClasses().contains(aClass)) {
            // class is mapped, return a TC that uses the store to load the object
            tc = new WokoTypeConverter(store);
            tc.setLocale(locale);
            return tc;
        }

        // special handling for RPC-formatted dates
        // we don't add the converter to the superclass' converters map
        // because it doesn't remove the old converter from the map before
        // putting the new one.
        if (Date.class.isAssignableFrom(aClass)) {
            tc = new WokoDateTypeConverter();
            tc.setLocale(locale);
            return tc;
        }

        // class is not managed by Woko, let Stripes find the converter
        return super.getTypeConverter(aClass, locale);
    }

}
