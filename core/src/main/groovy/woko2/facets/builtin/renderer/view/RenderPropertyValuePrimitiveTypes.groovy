package woko2.facets.builtin.renderer.view

import woko2.facets.builtin.all.RenderPropertyValueImpl

class RenderPropertyValuePrimitiveTypes extends RenderPropertyValueImpl {

  boolean matches(context) {
    return context?.object instanceof String ||
            context?.object instanceof Date ||
            context?.object instanceof Boolean ||
            context?.object instanceof Character ||
            context?.object instanceof Byte ||
            context?.object instanceof Short ||
            context?.object instanceof Integer ||
            context?.object instanceof Long ||
            context?.object instanceof Float ||
            context?.object instanceof Double
  }

}
