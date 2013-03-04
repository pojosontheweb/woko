package woko.facets.builtin;


import net.sourceforge.jfacets.IFacet;
import woko.facets.FragmentFacet;
import woko.facets.builtin.all.Link;

import java.util.List;

/**
 * <code>renderTabularListItemLinks</code> fragment facets are used to display links for
 * item's tabular list
 *
 * Return the links available in the RenderLinks facet whose default
 * generic behavior allows for CRUD operations, based on the presence of
 * CRUD facets on the target object (<code>view</code>, <code>edit</code> etc).
 */
public interface RenderTabularListItemLinks extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderTabularListItemLinks";

    /**
     * Return a list of links to be displayed for the item's tabular list
     * @return a list of links
     */
    List<Link> getLinks();

}