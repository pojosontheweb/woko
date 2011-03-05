package woko.actions

import net.sourceforge.stripes.controller.Intercepts
import net.sourceforge.stripes.controller.LifecycleStage
import net.sourceforge.stripes.validation.DefaultValidationMetadataProvider
import net.sourceforge.stripes.controller.Interceptor
import woko.util.WLogger
import net.sourceforge.stripes.validation.ValidationMetadata
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.action.ActionBean

@Intercepts([LifecycleStage.BindingAndValidation])
public class WokoValidationMedatataProvider extends DefaultValidationMetadataProvider implements Interceptor {

    private static ThreadLocal<WokoActionBean> wokoActionBeanHolder = new ThreadLocal<WokoActionBean>()

    private static final WLogger logger = WLogger.getLogger(WokoValidationMedatataProvider.class)

    private volatile int threadDelta = 0

    @Override
    public Map<String, ValidationMetadata> getValidationMetadata(Class<?> aClass) {
        Map<String,ValidationMetadata> original = super.getValidationMetadata(aClass);
        if (WokoActionBean.class.isAssignableFrom(aClass)) {
            Map<String,ValidationMetadata> all = new TreeMap<String,ValidationMetadata>();
            all.putAll(original);
            WokoActionBean wokoActionBean = wokoActionBeanHolder.get();
            if (wokoActionBean==null) {
                logger.debug("Unable to find WokoActionBean for current thread, using static validation only");
            } else {
                logger.debug("Computing dynamic validation metadata for " + wokoActionBean);
                def facet = wokoActionBean.getFacet();
                if (facet==null) {
                    // command facet not there, it's an error, we throw
                    throw new IllegalStateException("No CommandFacet for WokoActionBean " + wokoActionBean + ", this should never happen !");
                }
                logger.debug("Computing dynamic metadata for facet " + facet);
                Map<String, ValidationMetadata> commandFacetValidationInfos = this.getConfiguration()
                    .getValidationMetadataProvider().getValidationMetadata(facet.getClass());
                // append "facet" prefix to the property name
                for (String key : commandFacetValidationInfos.keySet()) {
                    ValidationMetadata validationMetadata = commandFacetValidationInfos.get(key);
                    all.put("facet." + key, validationMetadata);
                    logger.debug("  * added validation metadata : " + key + "=" + validationMetadata);
                }
            }
            return all;
        } else {
            return original;
        }
    }

    public Resolution intercept(ExecutionContext executionContext) throws Exception {
        ActionBean actionBean = executionContext.getActionBean();
        if (actionBean instanceof WokoActionBean) {
            // bind the action to thread local before binding and validation
            WokoActionBean wokoActionBean = (WokoActionBean)actionBean;
            try {
                wokoActionBeanHolder.set(wokoActionBean);
                threadDelta++;
                logger.debug("Assigning WokoActionBean to local thread : " + actionBean + ", request=" +
                        executionContext.getActionBeanContext().getRequest() + ", delta=" + threadDelta);
                return executionContext.proceed();
            } finally {
                wokoActionBeanHolder.remove();
                threadDelta--;
                logger.debug("Removing WokoActionBean from local thread : " + actionBean + ", request=" +
                        executionContext.getActionBeanContext().getRequest() + ", delta="+ threadDelta);
            }
        } else {
            // handle WokoActionBean only !
            return executionContext.proceed();
        }
    }

}
