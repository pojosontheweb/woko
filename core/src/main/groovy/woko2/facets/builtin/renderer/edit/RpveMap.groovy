package woko2.facets.builtin.renderer.edit

class RpveMap extends RpveBase {

  boolean matches(context) {
    return context?.object instanceof Map 
  }

}
