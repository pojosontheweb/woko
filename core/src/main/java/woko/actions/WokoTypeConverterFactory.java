package woko.actions;

import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;
import woko.Woko;
import woko.persistence.ObjectStore;

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
    // class is not managed by Woko, let Stripes find the converter
    return super.getTypeConverter(aClass, locale);
  }

}
