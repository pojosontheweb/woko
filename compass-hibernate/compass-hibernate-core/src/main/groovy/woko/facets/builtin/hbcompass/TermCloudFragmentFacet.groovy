package woko.facets.builtin.hbcompass

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFragmentFacet
import woko.hbcompass.tagcloud.CompassCloudElem
import woko.hbcompass.tagcloud.CompassCloud

@FacetKey(name="termCloudFragment", profileId="developer", targetObjectType=CompassCloud.class)
class TermCloudFragmentFacet extends BaseFragmentFacet {

  String getPath() {
    return '/WEB-INF/woko/jsp/hbcompass/term-cloud-fragment.jsp'
  }

    Collection<CompassCloudElem> getCloudElems() {
        facetContext.targetObject.getCloud(facetContext.woko.objectStore.compass)
    }


}
