package woko2.actions

import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.action.UrlBinding
import net.sourceforge.stripes.validation.Validate
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.DefaultHandler
import net.sourceforge.stripes.action.Before
import net.sourceforge.stripes.controller.LifecycleStage

import net.sourceforge.stripes.action.ActionBeanContext
import woko2.util.WLogger
import woko2.facets.ResolutionFacet

@UrlBinding('/{facetName}/{className}/{key}')
class WokoActionBean implements ActionBean {

  private static final WLogger logger = WLogger.getLogger(WokoActionBean.class)

  private WokoActionBeanContext context

  void setContext(ActionBeanContext context) {
    this.context = (WokoActionBeanContext)context
  }

  WokoActionBeanContext getContext() {
    return context
  }

  String className

  String key

  @Validate(required = true)
  String facetName

  private def object
  private ResolutionFacet facet

  def getObject() {
    return object
  }

  ResolutionFacet getFacet() {
    return facet
  }

  @Before(stages = [LifecycleStage.BindingAndValidation])
  void loadObjectAndFacet() {
    def req = context.request
    className = req.getParameter('className')
    facetName = req.getParameter('facetName')
    if (facetName==null) {
      facetName = 'view'
    }
    key = req.getParameter('key')
    logger.debug "Loading object for className=$className and key=$key"
    def woko = context.woko
    def objectStore = woko.objectStore
    object = objectStore.load(className,key)
    logger.debug "Loaded $object (className=$className, key=$key)"
    Class targetObjectClass
    if (object!=null) {
      targetObjectClass = object.getClass()
    } else {
      targetObjectClass = objectStore.getMappedClass(className);
      // fallback to new Object() if nothing else available
      if (targetObjectClass==null) {
        object = new Object()
        targetObjectClass = Object.class
      }
    }
    def f = woko.getFacet(facetName, req, object, targetObjectClass)
    if (!f) {
      def username = woko.getUsername(req)
      throw new IllegalStateException("Facet $facetName not found for user $username, className $className, key $key.")
    }
    if (!f instanceof ResolutionFacet) {
      throw new IllegalStateException("Facet $facet does not implement ResolutionFacet.")
    }
    facet = (ResolutionFacet)f
    context.request.setAttribute(facetName, facet);
    logger.debug "facet $facet loaded and bound to request"
  }

  @DefaultHandler
  Resolution execute() {
    Resolution result = facet.getResolution()
    if (result==null) {
      throw new IllegalStateException("Execution of facet $facet returned null !")
    }
    return result
  }

}
