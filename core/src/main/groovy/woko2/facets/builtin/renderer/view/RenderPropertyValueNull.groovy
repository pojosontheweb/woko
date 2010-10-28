package woko2.facets.builtin.renderer.view

class RenderPropertyValueNull extends RenderPropertyValue {

  boolean matches(context) {
    return context.object == null
  }


}
