package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.all.RenderPropertyValueImpl

class RenderPropertyValueIdAndType extends RenderPropertyValueImpl {

  boolean matches(context) {
    return context?.propertyName == '_id' || context?.propertyName == '_type'    
  }


}
