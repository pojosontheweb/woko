package facets

import woko.facets.BaseFacet
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.LocalizableMessage

@FacetKey(name="localization", profileId="all")
class Localization extends BaseResolutionFacet{

    @Override
    Resolution getResolution(ActionBeanContext abc) {
        abc.messages.add(new LocalizableMessage("all.localization.message"))
        return new ForwardResolution('/WEB-INF/jsp/localization.jsp')
    }
}
