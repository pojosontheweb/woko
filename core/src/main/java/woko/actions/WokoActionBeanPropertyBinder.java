package woko.actions;

import net.sourceforge.stripes.controller.DefaultActionBeanPropertyBinder;
import net.sourceforge.stripes.util.bean.PropertyExpressionEvaluation;

public class WokoActionBeanPropertyBinder extends DefaultActionBeanPropertyBinder {

    @Override
    protected boolean isBindingAllowed(PropertyExpressionEvaluation eval) {
        if (super.isBindingAllowed(eval)) {
            // do the woko specific binding checks
            Object actionBean = eval.getBean();
            Class<?> beanClass = actionBean.getClass();
            if (beanClass.equals(WokoActionBean.class)) {
                String source = eval.getExpression().getSource();
                if (source.startsWith("facet.") || source.startsWith("object.")) {
                    WokoActionBean wokoActionBean = (WokoActionBean)actionBean;

                    // check for @StrictBinding on the facet class
                    Object facet = wokoActionBean.getFacet();
                    if (facet!=null) {
                        Class<?> facetClass = facet.getClass();
                         if (!WokoFacetBindingPolicyManager.getInstance(facetClass).isBindingAllowed(eval)) {
                             return false;
                         }
                    }
                }
            }

        }
        return true;
    }
}
