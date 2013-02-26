package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.bootstrap.all.LayoutBootstrap

@FacetKey(name= WokoFacets.layout, profileId="all")
class MyLayout extends LayoutBootstrap {

    @Override
    String getAppTitle() {
        return "Bootstrap Skin"
    }

}
