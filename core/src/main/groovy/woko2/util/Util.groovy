package woko2.util

import net.sourceforge.stripes.util.ReflectUtil
import java.beans.PropertyDescriptor

class Util {

  static void assertArg(name, val) {
    if (val==null) {
      throw new IllegalArgumentException("The argument $name cannot be null")
    }
  }

  static def getPropertyNames(obj) {
    if (obj instanceof Map) {
      return obj.keySet()
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

}
