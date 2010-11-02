package woko2.util

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor
import javax.servlet.http.HttpServletRequest
import woko2.Woko
import woko2.facets.builtin.RenderPropertyValue
import woko2.facets.builtin.RenderPropertyValueEdit

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

  private static RenderPropertyValue getRenderFacet(String facetName, Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue, boolean throwIfNotFound) {
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)woko.getFacet(facetName + "_" + propertyName, request, owningObject);
    if (renderPropertyValue==null) {
      Class<?> pClass = propertyValue!=null ? propertyValue.getClass() : Util.getPropertyType(owningObject.getClass(), propertyName)
      renderPropertyValue =
            (RenderPropertyValue)woko.getFacet(facetName, request, propertyValue, pClass, throwIfNotFound)
    } else {
        request.setAttribute(facetName, renderPropertyValue)
    }
    if (renderPropertyValue!=null) {
      renderPropertyValue.setPropertyValue(propertyValue)
      renderPropertyValue.setOwningObject(owningObject)
      renderPropertyValue.setPropertyName(propertyName)      
    }
    return renderPropertyValue
  }

  static RenderPropertyValue getRenderPropValueFacet(Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue) {
    return getRenderFacet('renderPropertyValue', woko, request, owningObject, propertyName, propertyValue, true)
  }

  static RenderPropertyValue getRenderPropValueEditFacet(Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue) {
    String fName = 'renderPropertyValueEdit' // RenderPropertyValue.name returns 'renderPropertyValue' ???
    RenderPropertyValue renderPropertyValue = getRenderFacet(fName, woko, request, owningObject, propertyName, propertyValue, false)
    if (renderPropertyValue==null) {
        renderPropertyValue = getRenderFacet(RenderPropertyValue.name, woko, request, owningObject, propertyName, propertyValue, true)
    }
    renderPropertyValue.setPropertyValue(propertyValue)
    renderPropertyValue.setOwningObject(owningObject);
    renderPropertyValue.setPropertyName(propertyName);
    request.setAttribute(fName, renderPropertyValue)    
    return renderPropertyValue
  }

}
