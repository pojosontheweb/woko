package facets;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;

import java.util.Arrays;
import java.util.List;

@FacetKey(name="studioInJquery", profileId="developer")
public class WokoStudioWithJQuery extends BaseForwardResolutionFacet {

  public String getPath() {
    return "/WEB-INF/jsp/studioInJquery.jsp";
  }

  public List<FacetDescriptor> getFacetDescriptors() {
    IFacetDescriptorManager fdm = getFacetContext().getWoko().getJFacets().getFacetRepository().getFacetDescriptorManager();
    FacetDescriptor[] descriptors = fdm.getDescriptors();
    return Arrays.asList(descriptors);
  }

}
