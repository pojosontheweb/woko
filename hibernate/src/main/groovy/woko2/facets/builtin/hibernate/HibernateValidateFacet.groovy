package woko2.facets.builtin.hibernate

import woko2.facets.builtin.Validate
import woko2.facets.BaseFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.validation.SimpleError
import javax.validation.Validator
import javax.validation.Validation
import javax.validation.ConstraintViolation
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.actions.WokoActionBean

@FacetKey(name='validate',profileId='all')
class HibernateValidateFacet extends BaseFacet implements Validate {

  static final String OBJECT_PREFIX = "object."

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

  def boolean validate(ActionBeanContext abc) {
    // call hibernate validator and translate errors
    boolean hasErrors = false
    def errs = abc.validationErrors
    Set<ConstraintViolation> violations = validator.validate(context.targetObject)
    def ab = abc.request.getAttribute('actionBean')
    def abClass = ab ? ab.getClass() : WokoActionBean.class
    violations.each { v ->
      hasErrors = true
      def fieldName = OBJECT_PREFIX + v.propertyPath
      def stripesError = new SimpleError(v.message)
      stripesError.fieldName = fieldName
      stripesError.beanclass = abClass
      errs.add(fieldName, stripesError)      
    }
    return !hasErrors    
  }

}
