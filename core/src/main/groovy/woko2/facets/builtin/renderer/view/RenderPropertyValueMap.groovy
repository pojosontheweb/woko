package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.all.RenderPropertyValueImpl

class RenderPropertyValueMap extends RenderPropertyValueImpl {

  boolean matches(context) {
    return context?.object instanceof Map
  }

}
