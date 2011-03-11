package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import woko.facets.builtin.BaseResultFacet;
import woko.persistence.ListResultIterator;
import woko.persistence.ResultIterator;

import java.util.Collections;

@FacetKey(name="list", profileId="developer")
public class ListImpl extends BaseResultFacet implements woko.facets.builtin.ListObjects {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/list.jsp";
  }

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    String className = getClassName();
    if (className==null) {
      return new ListResultIterator(Collections.emptyList(), start, limit, 0);
    } else {
      return getFacetContext().getWoko().getObjectStore().list(className, start, limit);
    }
  }


}
