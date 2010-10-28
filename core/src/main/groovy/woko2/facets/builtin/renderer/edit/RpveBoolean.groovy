package woko2.facets.builtin.renderer.edit

class RpveBoolean extends RpveBase {

  boolean matches(context) {
    return context?.object instanceof Boolean 
  }

}
