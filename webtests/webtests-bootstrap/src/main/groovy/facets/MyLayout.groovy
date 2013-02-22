package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.actions.SwithThemeActionBean
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
        String baseBootstrap = "/css/bootstrap-v2.3.0/bootstrap.css"

        if (request){
            String theme = request.session.getAttribute(SwithThemeActionBean.THEME_COOKIE)
            if (theme) {
                baseBootstrap = "/css/$theme/bootstrap.css"
            }
        }

        return [
                baseBootstrap,
                "/css/responsive.css",
                "/css/woko.css"
        ]
    }

}
