package woko.facets.builtin;

import java.util.List;

/**
 * Base interface for tabular result facets : provide acces to the property names
 * to be displayed as columns of the resulting table.
 */
public interface TabularResultFacet {

    /**
     * Return the list of property names to be used as columns in the resulting table.
     * Note that search results can be polymorphic so be careful to the props you want to display.
     *
     * @return a list of property names
     */
    List<String> getPropertyNames();

}
