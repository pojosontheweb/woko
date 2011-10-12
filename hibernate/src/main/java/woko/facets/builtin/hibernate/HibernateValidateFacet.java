package woko.facets.builtin.hibernate;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;
import woko.actions.WokoActionBean;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.util.WLogger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@FacetKey(name="validate",profileId="all")
public class HibernateValidateFacet extends BaseFacet implements woko.facets.builtin.Validate {

    private static final Map<String, String> keyMapping = new HashMap<String, String>();
    static{
        keyMapping.put("javax.validation.constraints.AssertFalse.message", "validation.assertFalse");
        keyMapping.put("javax.validation.constraints.AssertTrue.message", "validation.assertTrue");
        keyMapping.put("javax.validation.constraints.DecimalMax.message", "validation.maxvalue.valueAboveMaximum");
        keyMapping.put("javax.validation.constraints.DecimalMin.message", "validation.minvalue.valueBelowMinimum");
        keyMapping.put("javax.validation.constraints.Digits.message", "validation.digits");
        keyMapping.put("javax.validation.constraints.Future.message", "validation.future");
        keyMapping.put("javax.validation.constraints.Max.message", "validation.maxvalue.valueAboveMaximum");
        keyMapping.put("javax.validation.constraints.Min.message", "validation.minvalue.valueBelowMinimum");
        keyMapping.put("javax.validation.constraints.NotNull.message", "validation.required.valueNotPresent");
        keyMapping.put("javax.validation.constraints.Null.message", "validation.null");
        keyMapping.put("javax.validation.constraints.Past.message", "validation.past");
        keyMapping.put("javax.validation.constraints.Pattern.message", "validation.pattern");
        keyMapping.put("javax.validation.constraints.Size.message", "validation.size");
        keyMapping.put("org.hibernate.validator.constraints.CreditCardNumber.message", "validator.creditCardNumber");
        keyMapping.put("org.hibernate.validator.constraints.Email.message", "validator.email");
        keyMapping.put("org.hibernate.validator.constraints.Length.message", "validator.length");
        keyMapping.put("org.hibernate.validator.constraints.NotBlank.message", "validator.notBlank");
        keyMapping.put("org.hibernate.validator.constraints.NotEmpty.message", "validator.notEmpty");
        keyMapping.put("org.hibernate.validator.constraints.Range.message", "validator.range");
        keyMapping.put("org.hibernate.validator.constraints.SafeHtml.message", "validator.safeHtml");
        keyMapping.put("org.hibernate.validator.constraints.ScriptAssert.message", "validator.scriptAssert");
        keyMapping.put("org.hibernate.validator.constraints.URL.message", "validator.url");
    }

    private static final String OBJECT_PREFIX = "object.";

    private static final WLogger log = WLogger.getLogger(HibernateValidateFacet.class);

    public boolean validate(ActionBeanContext abc) {
        // call hibernate validator and translate errors
        boolean hasErrors = false;
        ValidationErrors errs = abc.getValidationErrors();
        WokoFacetContext facetContext = getFacetContext();
        Object targetObject = facetContext.getTargetObject();
        ActionBean ab = (ActionBean)abc.getRequest().getAttribute("actionBean");
        Class<? extends ActionBean> abClass = ab!=null ? ab.getClass() : WokoActionBean.class;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(targetObject);

        for (ConstraintViolation<Object> c : constraintViolations){
            hasErrors = true;
            String fieldName = OBJECT_PREFIX + c.getPropertyPath();
            LocalizableError stripesError = new LocalizableError(getStripesKey(c.getMessageTemplate()), c.getConstraintDescriptor().getAttributes().values().toArray());
            stripesError.setFieldName(fieldName);
            stripesError.setBeanclass(abClass);
            errs.add(c.getPropertyPath().toString(), stripesError);
        }
        return !hasErrors;
    }

    private String getStripesKey(String hibernateKey){
        hibernateKey = hibernateKey.replaceAll("\\{", "");
        hibernateKey = hibernateKey.replaceAll("\\}", "");
        if (keyMapping.containsKey(hibernateKey))
            return keyMapping.get(hibernateKey);
        else
            return "hibernate.error.with.no.equivalence.in.stripes";
    }

}
