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

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.LocalizationBundleFactory;
import net.sourceforge.stripes.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class WokoLocalizationBundleFactory implements LocalizationBundleFactory {

    private static final String[] DEFAULT_BUNDLES = {"application","woko","stripes"};

    public static final String RESOURCE_BUNDLES_BASE_NAMES = "ResourceBundles.BaseNames";
    private String[] bundles;

    public ResourceBundle getFormFieldBundle(Locale locale) {
        return new WokoResourceBundle(locale, getBundleNames());
    }

    public ResourceBundle getErrorMessageBundle(Locale locale) {
        return new WokoResourceBundle(locale, getBundleNames());
    }

    public void init(Configuration config) {
        String bundleNames = config.getBootstrapPropertyResolver()
                .getProperty(RESOURCE_BUNDLES_BASE_NAMES);
        if (bundleNames==null) {
            bundles = DEFAULT_BUNDLES;
        } else {
            bundles = StringUtil.standardSplit(bundleNames);
        }
    }

    public List<String> getBundleNames() {
        return Arrays.asList(bundles);
    }
}
