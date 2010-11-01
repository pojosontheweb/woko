package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.SimpleMessage

import net.sourceforge.stripes.action.RedirectResolution
import woko2.util.WLogger
import net.sourceforge.jfacets.annotations.FacetKey

import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import woko2.facets.builtin.Validate
import woko2.facets.builtin.Save
import woko2.facets.builtin.Edit

@FacetKey(name='save', profileId='developer')
class SaveImpl extends BaseResolutionFacet implements Save {

  private final static WLogger logger = WLogger.getLogger(SaveImpl.class)

  Resolution getResolution(ActionBeanContext abc) {
    // try to find a validation facet for the object
    Validate validateFacet = (Validate)context.woko.getFacet(Validate.name, abc.request, context.targetObject, context.targetObject.getClass())
    if (validateFacet!=null) {
      logger.debug("Validation facet found, validating before saving...")
      if (validateFacet.validate(abc)) {
        doSave(abc)
      } else {
        logger.debug("Validate facet raised validation errors, not saving")
      }
    } else {
      logger.debug("No validation facet found, saving...")
      doSave(abc)
    }
    return new RedirectResolution(
            context.woko.facetUrl(
                    Edit.name,
                    context.targetObject))
  }

  protected void doSave(abc) {
    context.woko.objectStore.save(context.targetObject)
    abc.messages << new SimpleMessage('Object saved')
  }

}
