package net.sourceforge.stripes.rpc;

import net.sourceforge.stripes.action.Resolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RpcResolutionWrapper implements Resolution, RpcResolution {

    private final Resolution originalResolution;

    public RpcResolutionWrapper(Resolution originalResolution) {
        this.originalResolution = originalResolution;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        originalResolution.execute(request, response);
    }

}
