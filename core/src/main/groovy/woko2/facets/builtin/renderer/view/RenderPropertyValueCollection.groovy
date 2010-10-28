package woko2.facets.builtin.renderer.view

class RenderPropertyValueCollection extends RenderPropertyValue {

  boolean matches(context) {
    return context?.object instanceof Collection
  }

}
