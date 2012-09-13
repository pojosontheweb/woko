package facets

import woko.facets.builtin.all.RenderLinksImpl
import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.all.Link

@FacetKey(name="renderLinks",profileId="developer",targetObjectType=MyBook.class)
class RenderLinksMyBook extends RenderLinksImpl {
    @Override
    List<Link> getLinks() {
        def id = facetContext.targetObject._id
        List<Link> links = super.getLinks();
        links.each { l ->
            l.addAttribute("testMe", id)
        }

    }


}
