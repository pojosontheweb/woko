package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.all.RenderPropertyValueImpl

class RenderPropertyValueNull extends RenderPropertyValueImpl {

  boolean matches(context) {
    return context.object == null
  }


}
