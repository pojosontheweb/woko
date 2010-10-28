package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderObject extends BaseRendererFacet {

  def execute(context) {
    def res = createResult([object:context.object])
    logger.debug("Returning $res")
    return res
  }


}
