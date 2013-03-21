package woko.persistence;

import woko.util.Util;

public class DefaultAlternateKeyConverter implements AlternateKeyConverter {

    @Override
    public String convert(Object obj, Util.PropertyNameAndAnnotation<WokoAlternateKey> propAndAnnot, String propName, Object propValue) {
        Util.assertArg("obj", obj);
        Util.assertArg("propAndAnnot", propAndAnnot);
        Util.assertArg("propName", propName);
        Util.assertArg("propValue", propValue);

        return propValue.toString();
    }
}
