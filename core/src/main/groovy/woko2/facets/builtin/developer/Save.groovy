package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.SimpleMessage
import woko2.util.Util
import net.sourceforge.stripes.action.RedirectResolution
import woko2.util.WLogger
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.FacetConstants
import woko2.Woko
import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution

@FacetKey(name='save', profileId='developer')
class Save extends BaseResolutionFacet {

  private final static WLogger logger = WLogger.getLogger(Save.class)

  Resolution getResolution(ActionBeanContext abc) {
    doSave(abc)
    return new RedirectResolution(
            Util.facetUrl(
                    context.woko,
                    FacetConstants.edit,
                    context.targetObject))
  }

  protected void doSave(c) {
    ActionBeanContext abc = c.wokoActionBean.context
    abc.messages << new SimpleMessage('Object saved')
    c.woko.objectStore.save(c.object)
  }

}
