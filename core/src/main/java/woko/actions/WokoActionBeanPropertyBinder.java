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

package woko.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.DefaultActionBeanPropertyBinder;
import net.sourceforge.stripes.util.bean.PropertyExpressionEvaluation;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import woko.Woko;

import java.util.List;

/**
 * Woko-specific <code>ActionBeanPropertyBinder</code> : enables <code>@StrictBinding</code> to be used on
 * <code>ResolutionFacet</code>s, and thereby to restrict binding of <code>WokoActionBean</code>'s
 * <code>facet.*</code> and <code>object.*</code> properties.
 * Also replaces the prefixes with actual class names in Stripes errors, for easy localisation.
 *
 * @see WokoActionBean
 * @see WokoFacetBindingPolicyManager
 */
public class WokoActionBeanPropertyBinder extends DefaultActionBeanPropertyBinder {

    /**
     * Performs the Woko-specific binding checks
     * @param eval the expression to check for
     * @return <code>true</code> if binding is allowed, false otherwise.
     */
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

    /**
     * Replaces <code>facet</code> and <code>object</code> prefixes in stripes errors.
     */
    @Override
    public ValidationErrors bind(ActionBean bean, ActionBeanContext context, boolean validate) {
        ValidationErrors errorsSaved =  super.bind(bean, context, validate);
        ValidationErrors errorsUpdated = new ValidationErrors();
        if (bean instanceof WokoActionBean){
            Woko woko = Woko.getWoko(context.getServletContext());
            WokoActionBean wBean = (WokoActionBean)bean;
            String facetName = wBean.getFacetName();
            for(String key : errorsSaved.keySet()){
                List<ValidationError> errs = errorsSaved.get(key);
                if (key.startsWith("facet")){
                    String newKey = key.replaceFirst("facet", facetName);
                    errorsUpdated.put(newKey, errs);
                }else if (key.startsWith("object")){
                    Object o = wBean.getObject();
                    String className = o!=null ?
                            woko.getObjectStore().getClassMapping(o.getClass()) :
                            wBean.getClassName();
                    String newKey = key.replaceFirst("object", className);
                    errorsUpdated.put(newKey, errs);
                } else {
                    errorsUpdated.put(key, errs);
                }
            }
            return errorsUpdated;
        }
        else
            return errorsSaved;
    }
}
