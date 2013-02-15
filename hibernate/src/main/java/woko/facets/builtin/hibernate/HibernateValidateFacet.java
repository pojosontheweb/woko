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

package woko.facets.builtin.hibernate;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.localization.LocalizationUtility;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import woko.actions.WokoActionBean;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderPropertiesEdit;
import woko.facets.builtin.Validate;
import woko.hibernate.HibernateValidatorInterceptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

/**
 * <code>validate</code> facet that uses Hibernate Validator and javax.validation constraints.
 * Converts Hibernate Validator errors to Stripes Errors for transparent integration.
 */
@FacetKey(name = Validate.FACET_NAME, profileId = "all")
public class HibernateValidateFacet extends BaseFacet implements Validate {

    private static final String OBJECT_PREFIX = "object";

    /**
     * Delegate validation to hibernate validator and converts errors if any
     * @param abc the action bean context to add errors to
     * @return <code>true</code> if validation succeeded, <code>false</code> otherwise
     */
    public boolean validate(ActionBeanContext abc) {
        // call hibernate validator and translate errors
        boolean hasErrors = false;
        ValidationErrors errs = abc.getValidationErrors();
        WokoFacetContext facetContext = getFacetContext();
        Object targetObject = facetContext.getTargetObject();
        ActionBean ab = (ActionBean) abc.getRequest().getAttribute("actionBean");
        Class<? extends ActionBean> abClass = ab != null ? ab.getClass() : WokoActionBean.class;

        Validator validator = HibernateValidatorInterceptor.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(targetObject);

        // try to find prefix in renderPropertiesEdit for the object
        RenderPropertiesEdit rpe = (RenderPropertiesEdit)getWoko().getFacet(
                RenderPropertiesEdit.FACET_NAME, getRequest(), targetObject);
        String prefix = OBJECT_PREFIX;
        if (rpe!=null) {
            prefix = rpe.getFieldPrefix();
        }

        for (ConstraintViolation<Object> c : constraintViolations) {
            hasErrors = true;
            String fieldName = prefix + "." + c.getPropertyPath();
            String fieldKey = targetObject.getClass().getSimpleName() + "." + c.getPropertyPath();
            HibernateValidationError error = new HibernateValidationError(
                    c,
                    fieldKey
            );
            error.setFieldName(fieldName);
            error.setBeanclass(abClass);
            errs.add(fieldName, error);
        }
        return !hasErrors;
    }

    /**
     * Stripes SimpleError extension for converting Hibernate Validation errors to Stripes errors
     */
    static class HibernateValidationError extends SimpleError {

        private String fieldKey;
        private String humanReadableFieldName;

        public HibernateValidationError(ConstraintViolation<?> c, String fieldKey) {
            super(c.getMessage());
            this.fieldKey = fieldKey;
        }

        protected void resolveFieldName(Locale locale) {
            if (fieldKey == null) {
                humanReadableFieldName = "FIELD NAME NOT SUPPLIED IN CODE";
            } else {
                humanReadableFieldName =
                        LocalizationUtility.getLocalizedFieldName(fieldKey,
                                getActionPath(),
                                getBeanclass(),
                                locale);
                if (humanReadableFieldName == null) {
                    humanReadableFieldName = LocalizationUtility.makePseudoFriendlyName(fieldKey);
                }
            }
        }

        @Override
        public String getMessage(Locale locale) {
            String msg = super.getMessage(locale);
            return humanReadableFieldName + " " + msg;  // TODO hackish way to concat the field name to the actual message. see #136
        }
    }

}

