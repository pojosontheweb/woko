package woko.facets.builtin.push

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name = "push", profileId = "developer")
class Push extends BaseResolutionFacet {

    List<String> sources

    Resolution getResolution(ActionBeanContext abc) {
        StringBuilder sb = new StringBuilder()
        if (sources) {
            int i=0
            sources.each { s ->
                sb.append("source $i : \n").append("s").append("\n****\n")
                i++
            }
        } else {
            sb.append("No sources !")
        }
        return new StreamingResolution("text/plain", sb.toString())
    }

}
