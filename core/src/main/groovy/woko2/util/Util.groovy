package woko2.util

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor
import javax.servlet.http.HttpServletRequest
import woko2.Woko
import woko2.facets.builtin.RenderPropertyValue

class Util {

  static void assertArg(name, val) {
    if (val==null) {
      throw new IllegalArgumentException("The argument $name cannot be null")
    }
  }

  static List<String> getPropertyNames(obj, List<String> exclusions) {
    // use stripes reflect utils
    PropertyDescriptor[] descriptors = ReflectUtil.getPropertyDescriptors(obj.getClass())
    def res = []
    descriptors.each { PropertyDescriptor pd ->
      def name = pd.name
      if (!exclusions.contains(name)) {
        res << pd.name        
      }
    }
    return res
  }

  static Object getPropertyValue(Object obj, String propertyName) {
    try {
      return obj[propertyName]
    } catch(MissingPropertyException e) {
      throw new IllegalStateException("Property $propertyName doesn't existy on object $obj ! Make sure your renderProperties facet doesn't return incorrect property names.")
    }
  }

  static String firstCharLowerCase(String s) {
    return new String(s.charAt(0).toLowerCase()) + s.substring(1, s.length())
  }

  static String computePropertyPath(Stack<String> propChain) {
    StringBuilder sb = new StringBuilder()
    for (Iterator<String> it = propChain.iterator() ; it.hasNext() ; ) {
      sb.append(it.next())
      if (it.hasNext()) {
        sb.append(".")
      }
    }
    return sb.toString()
  }

  static Class<?> getPropertyType(Class owningObjectClass, String propertyName) {
    PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(owningObjectClass, propertyName)
    if (pd==null) {
      return null
    }
    return pd.getPropertyType()
  }

  static RenderPropertyValue getRenderPropValueFacet(Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue) {
    // try name override first
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)woko.getFacet(RenderPropertyValue.name + "_" + propertyName, request, owningObject);
    if (renderPropertyValue==null) {
      Class<?> pClass = propertyValue!=null ? propertyValue.getClass() : Util.getPropertyType(owningObject.getClass(), propertyName)
      renderPropertyValue =
            (RenderPropertyValue)woko.getFacet(RenderPropertyValue.name, request, propertyValue, pClass, true)
    } else {
        request.setAttribute(RenderPropertyValue.name, renderPropertyValue)
    }
    renderPropertyValue.setPropertyValue(propertyValue)
    renderPropertyValue.setOwningObject(owningObject)
    renderPropertyValue.setPropertyName(propertyName)
    return renderPropertyValue
  }

}
