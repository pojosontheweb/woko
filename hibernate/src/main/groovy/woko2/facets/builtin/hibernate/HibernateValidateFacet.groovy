package woko2.facets.builtin.hibernate

import woko2.facets.builtin.Validate
import woko2.facets.BaseFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.validation.SimpleError
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.actions.WokoActionBean
import org.hibernate.validator.ClassValidator
import org.hibernate.validator.InvalidValue

@FacetKey(name='validate',profileId='all')
class HibernateValidateFacet extends BaseFacet implements Validate {

  static final String OBJECT_PREFIX = "object."

  def boolean validate(ActionBeanContext abc) {
    // call hibernate validator and translate errors
    boolean hasErrors = false
    def errs = abc.validationErrors
    ClassValidator validator = new ClassValidator(facetContext.targetObject.getClass())
    InvalidValue[] invalidValues = validator.getInvalidValues(facetContext.targetObject)
    def ab = abc.request.getAttribute('actionBean')
    def abClass = ab ? ab.getClass() : WokoActionBean.class
    invalidValues.each { v ->
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
