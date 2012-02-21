package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@FacetKey(name= WokoFacets.create, profileId="developer")
public class Create extends BaseForwardResolutionFacet {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/create.jsp";
  }

  public List<String> getMappedClasses() {
    List<String> res = new ArrayList<String>();
    ObjectStore os = getFacetContext().getWoko().getObjectStore();
    List<Class<?>> mappedClasses = os.getMappedClasses();
    for (Class<?> c : mappedClasses) {
      if (!Modifier.isAbstract(c.getModifiers())) {
        res.add(os.getClassMapping(c));
      }
    }
    return res;
  }


}
