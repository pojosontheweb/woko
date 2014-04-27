/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.stripes.rpc;

import net.sourceforge.stripes.action.ActionBeanContext;
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
import org.json.JSONObject;
import woko.Woko;
import woko.facets.builtin.RenderObjectJson;
import woko.util.JsonResolution;

import javax.servlet.http.HttpServletRequest;

/**
 * Interceptor for RPC calls. Looks for a specific request param and handles
 * {@link RpcResolution} resolutions.
 */
@Intercepts({
        LifecycleStage.BindingAndValidation,
        LifecycleStage.CustomValidation,
        LifecycleStage.EventHandling
        })
public class RpcInterceptor implements Interceptor, ConfigurableComponent {

    private static final Log logger = Log.getInstance(RpcInterceptor.class);

    public static final String DEFAULT_RPC_PARAM_NAME = "isRpc";
    public static final String CFG_RPC_INTERCEPTOR_PARAM_NAME = "RpcInterceptor.Param.Name";

    private static String rpcParamName = DEFAULT_RPC_PARAM_NAME;

    public void init(Configuration configuration) throws Exception {
        // init rpcParamName from config
        rpcParamName = configuration.getBootstrapPropertyResolver().getProperty(CFG_RPC_INTERCEPTOR_PARAM_NAME);
        if (rpcParamName==null) {
            rpcParamName = DEFAULT_RPC_PARAM_NAME;
        }
        logger.info("Will handle RPC requests with param name ", rpcParamName);
    }

    public static boolean isRpcRequest(HttpServletRequest request) {
        return request.getParameter(rpcParamName) != null;
    }

    public Resolution intercept(ExecutionContext context) throws Exception {

        // process binding/validation/event
        Resolution result = context.proceed();

        // activate on special parameter
        if (isRpcRequest(context.getActionBeanContext().getRequest())) {
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
                        logger.debug("RPC Resolution not found for request ", context.getActionBeanContext().getRequest().getQueryString() + ", returning original resolution " + result);
                    }
                }
            }
        }
        return result;
    }

    protected Resolution serializeErrors(ExecutionContext context) {
        // serialize errors to JavaScript
        ActionBeanContext abc = context.getActionBeanContext();
        ValidationErrors errors = abc.getValidationErrors();
        Woko<?,?,?,?> woko = Woko.getWoko(abc.getServletContext());
        RenderObjectJson roj = woko.getFacet(RenderObjectJson.FACET_NAME, abc.getRequest(), errors);
        if (roj==null) {
            return new JavaScriptResolution(errors);
        }
        JSONObject jsonErrors = roj.objectToJson(abc.getRequest());
        return new JsonResolution(jsonErrors);
    }
}
