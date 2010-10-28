package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderTitle extends BaseRendererFacet {

  private def createTitleResult(title) {
    return createResult([title:title,showTitle:true])
  }

  def execute(context) {
    def o = context.object
    assert o
    def propNames = ['title', 'name', 'id', '_id']
    if (o instanceof Map) {
      for (String s : propNames) {
        if (o.containsKey(s)) {
          return createTitleResult(o[s])
        }
      }
    } else {
      for (String s : propNames) {
        try {
          return createTitleResult(o[s])
        } catch(MissingPropertyException) {
          // do nothing
        }
      }
    }

    // nothing matched, compute a meaningful title
    def objectStore = context?.woko?.objectStore
    assert objectStore
    def className = objectStore.getClassName(o)
    def key = objectStore.getKey(o)
    if (className && key) {
      return createTitleResult("$key@$className")
    }
    def res = createTitleResult(o.toString())
    logger.debug("Returning $res")
    return res    
  }

}
