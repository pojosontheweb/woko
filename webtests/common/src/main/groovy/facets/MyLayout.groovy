package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.all.LayoutAll

@FacetKey(name= WokoFacets.layout, profileId="all")
class MyLayout extends LayoutAll {

    @Override
    String getAppTitle() {
        return "Webtests"
    }

    @Override
    List<String> getCssIncludes() {
        return [
                "/woko/layout-all.css",
                "/woko/lithium/assets/style.css"
        ]
    }
}
