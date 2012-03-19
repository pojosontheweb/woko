/*
 * Copyright 2001-2010 Remi Vankeisbelck
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
