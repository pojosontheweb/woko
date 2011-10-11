package woko.facets;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;

public abstract class BaseForwardRpcResolutionFacet extends BaseForwardResolutionFacet {

    @Override
    public Resolution getResolution(final ActionBeanContext abc) {
        Resolution originalResolution =  super.getResolution(abc);
        return new RpcResolutionWrapper(originalResolution) {
            @Override
            public Resolution getRpcResolution() {
                return BaseForwardRpcResolutionFacet.this.getRpcResolution(abc);
            }
        };
    }

    protected abstract Resolution getRpcResolution(ActionBeanContext actionBeanContext);

}
