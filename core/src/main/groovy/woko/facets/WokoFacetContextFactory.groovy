package woko.facets

import net.sourceforge.jfacets.IFacetContextFactory
import net.sourceforge.jfacets.IFacetContext
import net.sourceforge.jfacets.IProfile
import net.sourceforge.jfacets.FacetDescriptor
import woko.Woko
import net.sourceforge.stripes.controller.Interceptor
import net.sourceforge.stripes.controller.LifecycleStage
import net.sourceforge.stripes.controller.Intercepts
import net.sourceforge.stripes.controller.ExecutionContext
import net.sourceforge.stripes.action.Resolution
import javax.servlet.http.HttpServletRequest

@Intercepts([LifecycleStage.RequestInit, LifecycleStage.RequestComplete])
class WokoFacetContextFactory implements IFacetContextFactory, Interceptor {

  private final Woko woko

  private ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>()

  def WokoFacetContextFactory(Woko woko) {
    this.woko = woko;
  }

  IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
    return new WokoFacetContext(name, profile, targetObject, facetDescriptor, woko, requests.get())
  }

  Resolution intercept(ExecutionContext context) throws Exception {
    LifecycleStage stage = context.lifecycleStage
    HttpServletRequest request = context.actionBeanContext.request
    if (stage==LifecycleStage.RequestInit) {
      requests.set(request)
    } else if (stage.equals(LifecycleStage.RequestComplete)) {
      requests.remove()
    }
    try {
      return context.proceed();
    } catch(Exception e) {
      throw e
    }
  }

}
