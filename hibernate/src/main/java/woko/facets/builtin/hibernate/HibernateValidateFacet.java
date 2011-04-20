package woko.facets.builtin.hibernate;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;

@FacetKey(name="validate",profileId="all")
public class HibernateValidateFacet extends BaseFacet implements woko.facets.builtin.Validate {

  private static final String OBJECT_PREFIX = "object.";

  public boolean validate(ActionBeanContext abc) {
    // call hibernate validator and translate errors
    boolean hasErrors = false;
    ValidationErrors errs = abc.getValidationErrors();
    WokoFacetContext facetContext = getFacetContext();
    Object targetObject = facetContext.getTargetObject();
    ClassValidator validator = new ClassValidator(targetObject.getClass());
    InvalidValue[] invalidValues = validator.getInvalidValues(targetObject);
    ActionBean ab = (ActionBean)abc.getRequest().getAttribute("actionBean");
    Class<? extends ActionBean> abClass = ab!=null ? ab.getClass() : null;
    if (abClass!=null) {
      for (InvalidValue v : invalidValues) {
        hasErrors = true;
        String fieldName = OBJECT_PREFIX + v.getPropertyPath();
        SimpleError stripesError = new SimpleError(v.getMessage());
        stripesError.setFieldName(fieldName);
        stripesError.setBeanclass(abClass);
        errs.add(fieldName, stripesError);
      }
      return !hasErrors;
    }
    return true;
  }

}
