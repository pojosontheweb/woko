package woko.facets.builtin;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseForwardResolutionFacet;
import woko.persistence.ResultIterator;

public abstract class BaseResultFacet extends BaseForwardResolutionFacet implements ResultFacet {

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

  protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit);

}
