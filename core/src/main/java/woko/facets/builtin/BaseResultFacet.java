package woko.facets.builtin;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.persistence.ResultIterator;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseResultFacet extends BaseForwardRpcResolutionFacet implements ResultFacet {

  private Integer resultsPerPage = 10;
  private Integer page = 1;
  private String className;

  private ResultIterator resultIterator;

  public Integer getResultsPerPage() {
    return resultsPerPage;
  }

  public void setResultsPerPage(Integer resultsPerPage) {
    this.resultsPerPage = resultsPerPage;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public String getClassName() {
    return className;
  }

  public ResultIterator getResults() {
    return resultIterator;
  }

  public Resolution getResolution(ActionBeanContext abc) {
    className = abc.getRequest().getParameter("className");
    page = page==null ? 1 : page;
    resultsPerPage = resultsPerPage==null ? 10 : resultsPerPage;
    int start = (page-1) * resultsPerPage;
    int limit = resultsPerPage;
    resultIterator = createResultIterator(abc, start, limit);
    return super.getResolution(abc);
  }

    @Override
    protected Resolution getRpcResolution(final ActionBeanContext actionBeanContext) {
        try {
            ResultIterator resultIterator = getResults();
            JSONObject r = new JSONObject();
            r.put("totalSize", resultIterator.getTotalSize());
            r.put("start", resultIterator.getStart());
            r.put("limit", resultIterator.getLimit());
            JSONArray items = new JSONArray();
            final WokoFacetContext facetContext = getFacetContext();
            final HttpServletRequest request = actionBeanContext.getRequest();
            while (resultIterator.hasNext()) {
                Object o = resultIterator.next();
                if (o==null) {
                    items.put((JSONObject)null);
                } else {
                    RenderObjectJson roj =
                    (RenderObjectJson)facetContext.getWoko().getFacet("renderObjectJson", request, o);
                    JSONObject jo = roj.objectToJson(request);
                    items.put(jo);
                }
            }
            r.put("items", items);
            return new StreamingResolution("text/json", r.toString());
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
    }

  protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit);

}
