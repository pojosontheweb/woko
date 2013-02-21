package webtests.usermanagement.facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.all.LayoutAll

@FacetKey(name= WokoFacets.layout, profileId="all")
class MyLayout extends LayoutAll {

    @Override
    String getAppTitle() {
        return "Bootstrap Skin"
    }

    @Override
    List<String> getCssIncludes() {
        return [
                "/css/bootstrap-v2.3.0/bootstrap.css",
                "/css/responsive.css",
                "/css/woko.css"
        ]
    }
}
