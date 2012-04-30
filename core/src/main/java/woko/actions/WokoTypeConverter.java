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

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import java.util.Collection;
import java.util.Locale;

public class WokoTypeConverter implements TypeConverter<Object> {

  private static final WLogger logger = WLogger.getLogger(WokoTypeConverter.class);

  private final ObjectStore store;
  private Locale locale;

  public WokoTypeConverter(ObjectStore store) {
    this.store = store;
  }

  public Object convert(String value, Class<? extends Object> targetType, Collection<ValidationError> errors) {
    logger.debug("Using ObjectStore '" + store + "' to convert value '" + value + "' for type " + targetType);
    return store.load(store.getClassMapping(targetType), value);
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

}
