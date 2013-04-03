package woko.actions;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.format.DefaultFormatterFactory;
import net.sourceforge.stripes.format.Formatter;
import woko.Woko;
import woko.persistence.ObjectStore;

import java.util.List;
import java.util.Locale;

public class WokoFormatterFactory extends DefaultFormatterFactory {

    private Woko<?,?,?,?> woko = null;

    @Override
    public void init(Configuration configuration) throws Exception {
        super.init(configuration);
        woko = Woko.getWoko(getConfiguration().getServletContext());
    }

    @Override
    public Formatter<?> getFormatter(Class<?> clazz, Locale locale, String formatType, String formatPattern) {
        if (woko!=null) {
            ObjectStore store = woko.getObjectStore();
            List<Class<?>> mappedClasses = store.getMappedClasses();
            for (Class<?> mappedClass : mappedClasses) {
                if (mappedClass.isAssignableFrom(clazz)) {
                    // class is mapped, return a TC that uses the store to load the object
                    return new WokoFormatter(woko);
                }
            }
        }
        // class ain't mapped in store, use default formatter
        return super.getFormatter(clazz, locale, formatType, formatPattern);
    }

}
