package woko.actions

import net.sourceforge.stripes.validation.TypeConverter
import net.sourceforge.stripes.validation.DefaultTypeConverterFactory
import woko.Woko
import woko.persistence.ObjectStore

class WokoTypeConverterFactory extends DefaultTypeConverterFactory {

  TypeConverter getTypeConverter(Class aClass, Locale locale) {
    // check if the class is a Woko mapped class
    TypeConverter tc
    ObjectStore store = Woko.getWoko(configuration.servletContext).objectStore
    if (store.getMappedClasses().contains(aClass)) {
      // class is mapped, return a TC that uses the store to load the object
      tc = new WokoTypeConverter(store)
      tc.locale = locale
      return tc
    }
    // class is not managed by Woko, let Stripes find the converter
    return super.getTypeConverter(aClass, locale)
  }

}
