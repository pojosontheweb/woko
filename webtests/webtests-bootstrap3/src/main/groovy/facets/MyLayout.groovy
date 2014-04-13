package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.bootstrap3.all.LayoutBootstrap3

@FacetKey(name= WokoFacets.layout, profileId="all")
class MyLayout extends LayoutBootstrap3 {

    @Override
    String getAppTitle() {
        return "Bootstrap3 Skin"
    }

}
