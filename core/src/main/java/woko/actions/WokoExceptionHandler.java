package woko.actions;

import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;
import woko.facets.FacetNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WokoExceptionHandler extends DefaultExceptionHandler {

  public Resolution handleFacetNotFoundException(FacetNotFoundException exc, HttpServletRequest request, HttpServletResponse response) {
      return new ErrorResolution(404);
  }

}
