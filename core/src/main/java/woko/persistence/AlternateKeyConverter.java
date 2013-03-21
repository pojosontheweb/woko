package woko.persistence;

import woko.util.Util;

public interface AlternateKeyConverter {

    String convert(Object obj, Util.PropertyNameAndAnnotation<WokoAlternateKey> propAndAnnot, String propName, Object propValue);

}
