package webtests.async

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets
import woko.facets.builtin.bootstrap.all.LayoutBootstrap

@FacetKey(name= WokoFacets.layout, profileId="all")
class AsyncLayout extends LayoutBootstrap {

    @Override
    String getAppTitle() {
        return "Async tests"
    }

}
