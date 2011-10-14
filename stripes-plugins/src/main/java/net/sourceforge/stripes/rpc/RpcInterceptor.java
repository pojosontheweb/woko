package net.sourceforge.stripes.rpc;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.ajax.JavaScriptResolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.ValidationErrors;

@Intercepts({
        LifecycleStage.BindingAndValidation,
        LifecycleStage.CustomValidation,
        LifecycleStage.EventHandling
        })
public class RpcInterceptor implements Interceptor, ConfigurableComponent {

    private static final Log logger = Log.getInstance(RpcInterceptor.class);

    public static final String DEFAULT_RPC_PARAM_NAME = "isRpc";
    public static final String CFG_RPC_INTERCEPTOR_PARAM_NAME = "RpcInterceptor.Param.Name";

    private String rpcParamName;

    public void init(Configuration configuration) throws Exception {
        // init rpcParamName from config
        rpcParamName = configuration.getBootstrapPropertyResolver().getProperty(CFG_RPC_INTERCEPTOR_PARAM_NAME);
        if (rpcParamName==null) {
            rpcParamName = DEFAULT_RPC_PARAM_NAME;
        }
        logger.info("Will handle RPC requests with param name ", rpcParamName);
    }

    protected boolean isRpcRequest(ExecutionContext context) {
        // activate on special parameter
        return context.getActionBeanContext().getRequest().getParameter(rpcParamName) != null;
    }

    public Resolution intercept(ExecutionContext context) throws Exception {

        // process binding/validation/event
        Resolution result = context.proceed();

        // activate on special parameter
        if (isRpcRequest(context)) {
            logger.debug("Request ", context.getActionBeanContext().getRequest().getQueryString(), " considered RPC");
            // do we have errors ?
            ValidationErrors errors =
                    context.getActionBeanContext().getValidationErrors();
            if (errors.size() > 0) {
                // validation errors found, return them serialized
                result = serializeErrors(context);
                logger.debug("Returning serialized errors for RPC request ", context.getActionBeanContext().getRequest().getQueryString());
            } else {
                // no validation errors, has already been handled ?
                if (context.getLifecycleStage().equals(LifecycleStage.EventHandling)) {
                    // event already handled, we return the RPC resolution if there's one
                    if (result instanceof RpcResolution) {
                        RpcResolution rpcResolution = (RpcResolution)result;
                        Resolution rpcResult = rpcResolution.getRpcResolution();
                        if (rpcResult==null) {
                            logger.warn("RPC request, but rpc resolution is null. Will return the original resolution.");
                        } else {
                            result = rpcResult;
                            logger.debug("RPC Resolution found for request ", context.getActionBeanContext().getRequest().getQueryString() + ", returning " + result);
                        }
                    } else {
                        // not an RpcResolution, return the regular resolution
                        logger.warn("RPC Resolution not found for request ", context.getActionBeanContext().getRequest().getQueryString() + ", returning original resolution " + result);
                    }
                }
            }
        }
        return result;
    }

    protected Resolution serializeErrors(ExecutionContext context) {
        // serialize errors to JavaScript
        ValidationErrors errors =
                    context.getActionBeanContext().getValidationErrors();
        return new JavaScriptResolution(errors);
    }
}
