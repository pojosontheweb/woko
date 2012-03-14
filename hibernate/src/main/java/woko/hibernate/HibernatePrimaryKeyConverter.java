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

package woko.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import woko.util.Util;
import woko.util.WLogger;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

public class HibernatePrimaryKeyConverter {

  private static final WLogger logger = WLogger.getLogger(HibernateTxInterceptor.class);

  public Serializable convert(String value, Class<?> targetConversionType) {
    logger.debug("converting String value=$value to type " + targetConversionType.getName() + "...");
    Serializable result = null;
    if (Number.class.isAssignableFrom(targetConversionType)) {
      try {
        result = NumberFormat.getInstance().parse(value);
      } catch (ParseException e) {
        logger.error("Unable to parse Number from str " + value, e);
        return null;
      }
    } else if (Date.class.isAssignableFrom(targetConversionType)) {
      try {
        result = DateFormat.getInstance().parse(value);
      } catch (ParseException e) {
        logger.error("Unable to parse Date from str $value", e);
        return null;
      }
    } else if (String.class.equals(targetConversionType)) {
      result = value;
    } else {
      String msg = "Supplied primary key type " + targetConversionType.getName() + " is not supported.";
      logger.error(msg);
      throw new UnsupportedOperationException(msg);
    }
    if (result != null) {
      logger.debug("converted String value=" + value + " to " + result + ", class=" + result.getClass());
    }
    return result;
  }

  public Serializable getPrimaryKeyValue(SessionFactory sessionFactory, Object entity, Class<?> entityClass) {
    ClassMetadata cm = sessionFactory.getClassMetadata(entityClass);
    if (cm == null) {
      return null;
    }
    String idPropName = cm.getIdentifierPropertyName();
    if (idPropName == null) {
      return null;
    }
    return (Serializable) Util.getPropertyValue(entity, idPropName);
  }


}
