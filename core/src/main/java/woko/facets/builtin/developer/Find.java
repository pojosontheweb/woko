package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name = WokoFacets.find, profileId = "developer")
public class Find extends BaseForwardResolutionFacet {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/find.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
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