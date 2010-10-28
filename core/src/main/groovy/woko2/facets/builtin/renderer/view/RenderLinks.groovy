package woko2.facets.builtin.renderer.view

import woko2.Woko
import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderLinks extends BaseRendererFacet {

  def execute(context) {
    def links = []
    Woko woko = context.woko
    def o = context.object

    // display edit link if object can be edited
    def editCtx = [:]
    editCtx.putAll(context)
    editCtx.facetName = "edit"
    def editFacet = woko.getFacet(editCtx)
    if (editFacet) {
      String className = woko.objectStore.getClassName(o)
      String key = woko.objectStore.getKey(o)
      links << [href:"edit/$className/$key",text:'edit']
    }

    // display delete link if object can be deleted
    def deleteCtx = [:]
    deleteCtx.putAll(context)
    deleteCtx.facetName = "delete"
    def deleteFacet = woko.getFacet(context)
    if (deleteFacet) {
      String className = woko.objectStore.getClassName(o)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'delete']
    }

    return createResult([links:links])    
  }

}
