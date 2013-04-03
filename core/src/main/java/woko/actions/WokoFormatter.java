package woko.actions;

import net.sourceforge.stripes.format.Formatter;
import woko.Woko;

import java.util.Locale;

public class WokoFormatter implements Formatter<Object> {

    private final Woko<?,?,?,?> woko;

    public WokoFormatter(Woko<?, ?, ?, ?> woko) {
        this.woko = woko;
    }

    @Override
    public void setFormatType(String formatType) {
    }

    @Override
    public void setFormatPattern(String formatPattern) {
    }

    @Override
    public void setLocale(Locale locale) {
    }

    @Override
    public void init() {
    }

    @Override
    public String format(Object input) {
        if (input==null) {
            return null;
        }
        String key = woko.getObjectStore().getKey(input);
        if (key==null) {
            return input.toString();
        }
        return key;
    }
}
