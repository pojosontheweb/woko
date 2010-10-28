package woko2.facets.builtin.renderer.view

class RenderPropertyValueMap extends RenderPropertyValue {

  boolean matches(context) {
    return context?.object instanceof Map
  }

}
