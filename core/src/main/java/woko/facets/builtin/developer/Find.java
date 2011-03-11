package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.persistence.ObjectStore;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name="find", profileId="developer")
public class Find extends BaseForwardResolutionFacet {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/find.jsp";
  }

  public List<String> getMappedClasses() {
    List<String> res = new ArrayList<String>();
    ObjectStore os = getFacetContext().getWoko().getObjectStore();
    List<Class<?>> mappedClasses = os.getMappedClasses();
    for (Class<?> c : mappedClasses) {
      res.add(os.getClassMapping(c));
    }
    return res;
  }



}