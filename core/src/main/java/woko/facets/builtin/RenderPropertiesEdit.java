package woko.facets.builtin;

import java.util.List;
import java.util.Map;

public interface RenderPropertiesEdit extends RenderProperties {

    public static final String FACET_NAME = RenderProperties.FACET_NAME +  "Edit";

    boolean isPartialForm();

    String getFieldPrefix();

    /**
     * Return map of hidden parameters. Those params will be
     * written in the FORM with the regular object properties.
     *
     * Can return null or an empty map (no specific hidden inputs will be written - default behavior).
     *
     * Values in the map can also be null : then it'll just generate a s:hidden and
     * let Stripes bind the value.
     *
     * @return a Map of key/value hidden params
     */
    Map<String,Object> getHiddenFields();

    /**
     * Return a list of read-only properties for the object. Those properties
     * will be rendered using renderPropertyValue as if they were by renderProperties.
     * @return a list of Strings
     */
    public List<String> getReadOnlyPropertyNames();
}
