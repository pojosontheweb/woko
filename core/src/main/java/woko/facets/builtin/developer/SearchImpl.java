package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import woko.facets.builtin.BaseResultFacet;
import woko.facets.builtin.Search;
import woko.persistence.ListResultIterator;
import woko.persistence.ResultIterator;

import java.util.Collections;

@FacetKey(name="search", profileId="developer")
public class SearchImpl extends BaseResultFacet implements Search {

  private String query;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/search.jsp";
  }

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    if (query==null) {
      return new ListResultIterator(Collections.emptyList(), start, limit, 0);
    } else {
      return getFacetContext().getWoko().getObjectStore().search(query, start, limit);
    }
  }

}
