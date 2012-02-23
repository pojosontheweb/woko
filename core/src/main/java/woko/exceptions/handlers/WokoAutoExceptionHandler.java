package woko.exceptions.handlers;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.AutoExceptionHandler;
import net.sourceforge.stripes.exception.StripesServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WokoAutoExceptionHandler implements AutoExceptionHandler {

    public Resolution handle(Exception e, HttpServletRequest request, HttpServletResponse response) {
        return new ForwardResolution("/WEB-INF/woko/jsp/generic-exception.jsp");
    }


}
