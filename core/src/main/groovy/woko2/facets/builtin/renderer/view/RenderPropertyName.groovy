package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderPropertyName extends BaseRendererFacet {

  def execute(context) {
    def res = createResult([propertyValue:context.object,propertyName:context.propertyName])
    logger.debug("Returning $res")
    return res
  }


}
