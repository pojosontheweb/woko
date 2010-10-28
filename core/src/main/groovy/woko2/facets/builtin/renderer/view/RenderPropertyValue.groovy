package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderPropertyValue extends BaseRendererFacet {

  String getName() {
    return 'renderPropertyValue'
  }

  def execute(context) {
    def propertyChain = context?.propertyChain
    if (propertyChain==null) {
      propertyChain = []
    }
    propertyChain << context.propertyName
    def res = createResult([
            propertyValue:context.object,
            propertyName:context.propertyName,
            propertyChain:propertyChain
    ])    
    logger.debug("Returning $res")
    return res
  }


}
