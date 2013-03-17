package woko.ext.categories.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.categories.Category;
import woko.facets.builtin.RenderProperties;
import woko.facets.builtin.all.RenderPropertiesImpl;

@FacetKey(name = RenderProperties.FACET_NAME, profileId = "categorymanager", targetObjectType = Category.class)
public class RenderCategoryPropertiesCategMgr extends RenderPropertiesImpl {
}
