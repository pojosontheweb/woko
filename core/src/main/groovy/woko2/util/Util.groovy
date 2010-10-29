package woko2.util

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor
import javax.servlet.http.HttpServletRequest

class Util {

  static void assertArg(name, val) {
    if (val==null) {
      throw new IllegalArgumentException("The argument $name cannot be null")
    }
  }

  static List<String> getPropertyNames(obj) {
    if (obj instanceof Map) {
      return new ArrayList(obj.keySet())
    }
    // use stripes reflect utils
    PropertyDescriptor[] descriptors = ReflectUtil.getPropertyDescriptors(obj.getClass())
    def res = []
    descriptors.each { PropertyDescriptor pd ->
      res << pd.name
    }
    return res
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

  public static Class<?> getPropertyType(Class owningObjectClass, String propertyName) {
    PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(owningObjectClass, propertyName)
    if (pd==null) {
      return null
    }
    return pd.getPropertyType()
  }

}
