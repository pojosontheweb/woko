package woko.facets.builtin.developer

import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.SimpleMessage

import net.sourceforge.stripes.action.RedirectResolution
import woko.util.WLogger
import net.sourceforge.jfacets.annotations.FacetKey

import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import woko.facets.builtin.Validate
import woko.facets.builtin.Save
import woko.facets.builtin.Edit
import net.sourceforge.stripes.action.ForwardResolution

@FacetKey(name='save', profileId='developer')
class SaveImpl extends BaseResolutionFacet implements Save {

  private final static WLogger logger = WLogger.getLogger(SaveImpl.class)

  Resolution getResolution(ActionBeanContext abc) {
    // try to find a validation facet for the object
    Validate validateFacet = (Validate)facetContext.woko.getFacet('validate', abc.request, facetContext.targetObject, facetContext.targetObject.getClass())
    if (validateFacet!=null) {
      logger.debug("Validation facet found, validating before saving...")
      if (validateFacet.validate(abc)) {
        doSave(abc)
      } else {
        logger.debug("Validate facet raised validation errors, not saving")
        // forward to the edit fragment
        Edit editFacet = (Edit)facetContext.woko.getFacet('edit', abc.request, facetContext.targetObject, facetContext.targetObject.getClass(), true)
        return new ForwardResolution(editFacet.getFragmentPath())
      }
    } else {
      logger.debug("No validation facet found, saving...")
      doSave(abc)
    }
    return new RedirectResolution(
            facetContext.woko.facetUrl(
                    'edit',
                    facetContext.targetObject))
  }

  protected void doSave(abc) {
    facetContext.woko.objectStore.save(facetContext.targetObject)
    abc.messages << new SimpleMessage('Object saved')
  }

}
