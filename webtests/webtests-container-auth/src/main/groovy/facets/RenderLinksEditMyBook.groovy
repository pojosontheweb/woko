package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.all.Link
import woko.facets.builtin.all.RenderLinksImpl
import woko.facets.builtin.all.RenderLinksEditImpl

@FacetKey(name="renderLinksEdit",profileId="developer",targetObjectType=MyBook.class)
class RenderLinksEditMyBook extends RenderLinksEditImpl {

    @Override
    List<Link> getLinks() {
        def id = facetContext.targetObject._id
        List<Link> links = super.getLinks();
        links.each { l ->
            l.addAttribute("testMeEdit", id)
        }

    }


}
