package woko.actions

import net.sourceforge.stripes.validation.TypeConverter
import net.sourceforge.stripes.validation.ValidationError
import woko.persistence.ObjectStore
import woko.util.WLogger

class WokoTypeConverter implements TypeConverter<Object> {

  private static final WLogger logger = WLogger.getLogger(WokoTypeConverter.class)

  private final ObjectStore store
  private Locale locale

  WokoTypeConverter(ObjectStore store) {
    this.store = store
  }

  Object convert(String value, Class<? extends Object> targetType, Collection<ValidationError> errors) {
    logger.debug("Using ObjectStore '$store' to convert value '$value' for type $targetType")
    return store.load(store.getClassMapping(targetType), value)
  }

  void setLocale(Locale locale) {
    this.locale = locale
  }

}
