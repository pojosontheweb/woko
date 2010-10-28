package woko2.facets.builtin.renderer.view

class RenderPropertyValueIdAndType extends RenderPropertyValue {

  boolean matches(context) {
    return context?.propertyName == '_id' || context?.propertyName == '_type'    
  }


}
