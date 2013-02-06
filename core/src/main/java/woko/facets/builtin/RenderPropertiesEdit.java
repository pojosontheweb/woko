package woko.facets.builtin;

import java.util.List;
import java.util.Map;

/**
 * <code>renderPropertiesEdit</code> fragment facets are the counterpart of <code>renderProperties</code> for
 * objects displayed in edit mode. They render the properties of the target object as a FORM so that
 * the user can modifiy the state.
 *
 * One can override this facet in order to customize the editable properties section
 * for the various classes and roles of the app.
 */
public interface RenderPropertiesEdit extends RenderProperties {

    public static final String FACET_NAME = RenderProperties.FACET_NAME +  "Edit";

    /**
     * Partial FORM handling (useful if the fragment is used inside a FORM already)
     * @return <code>true</code> if you want a partial FORM (no additional FORM element), <code>false</code> otherwise (regular FORM)
     */
    boolean isPartialForm();

    /**
     * Return the field prefix to be used for HTML FORM elements. Defaults to "object."
     */
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
