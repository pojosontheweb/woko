package woko.facets.builtin.hbcompass;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.hbcompass.HibernateCompassStore;
import woko.hbcompass.tagcloud.CompassCloud;
import woko.hbcompass.tagcloud.CompassCloudElem;

import java.util.Collection;

@FacetKey(name = "termCloudFragment", profileId = "developer", targetObjectType = CompassCloud.class)
public class TermCloudFragmentFacet extends BaseFragmentFacet {

  public String getPath() {
    return "/WEB-INF/woko/jsp/hbcompass/term-cloud-fragment.jsp";
  }

  public Collection<CompassCloudElem> getCloudElems() {
    WokoFacetContext facetContext = getFacetContext();
    HibernateCompassStore hbc = (HibernateCompassStore) facetContext.getWoko().getObjectStore();
    return ((CompassCloud) facetContext.getTargetObject()).getCloud(hbc.getCompass());
  }


}
