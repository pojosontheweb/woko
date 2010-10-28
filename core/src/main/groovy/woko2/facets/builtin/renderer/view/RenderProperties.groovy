package woko2.facets.builtin.renderer.view

import woko2.util.Util
import woko2.facets.builtin.renderer.BaseRendererFacet

class RenderProperties extends BaseRendererFacet {

  def getPropertyNames(object) {
    return Util.getPropertyNames(object)
  }

  def execute(context) {
    def propNames = getPropertyNames(context.object)
    def properties = [:]
    for (String pName : propNames) {
      properties[pName] = context.object[pName]
    }
    def res = createResult([properties:properties,propertyNames:properties.keySet(),owningObject:context.object])
    logger.debug("Returning $res")
    return res
  }


}
