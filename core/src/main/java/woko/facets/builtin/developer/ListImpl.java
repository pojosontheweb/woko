package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.BaseResultFacet;
import woko.facets.builtin.RenderObjectJson;
import woko.persistence.ListResultIterator;
import woko.persistence.ResultIterator;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@FacetKey(name="list", profileId="developer")
public class ListImpl extends BaseResultFacet implements woko.facets.builtin.ListObjects {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/list.jsp";
  }

  protected ResultIterator<?> createResultIterator(ActionBeanContext abc, int start, int limit) {
    String className = getClassName();
    if (className==null) {
      return new ListResultIterator<Object>(Collections.emptyList(), start, limit, 0);
    } else {
      return getFacetContext().getWoko().getObjectStore().list(className, start, limit);
    }
  }

}
