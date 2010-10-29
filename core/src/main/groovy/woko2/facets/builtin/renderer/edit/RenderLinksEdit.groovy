package woko2.facets.builtin.renderer.edit

import woko2.Woko
import woko2.facets.builtin.all.RenderLinksImpl

class RenderLinksEdit extends RenderLinksImpl {

  def execute(context) {
    def links = []
    Woko woko = context.woko
    def o = context.object
    def viewCtx = [:]
    viewCtx.putAll(context)
    viewCtx.facetName = 'view'
    def viewFacet = woko.getFacet(context)
    if (viewFacet) {
      String className = woko.objectStore.getClassName(o)
      String key = woko.objectStore.getKey(o)
      links << [href:"view/$className/$key",text:'cancel']
    }
    return createResult([links:links])    
  }


}
