package woko2.facets.builtin.renderer

import woko2.util.Util

abstract class BaseRendererFacet {

  protected String getFragmentPath() {
    String fqcn = getClass().getName()
    int indexOfLastDot = fqcn.lastIndexOf('.')
    String pkg = fqcn.substring(0, indexOfLastDot)
    int i = pkg.indexOf('renderer')
    pkg = pkg.substring(i+9, pkg.length())
    pkg = pkg.replaceAll(/\./, "/")    
    String className = Util.firstCharLowerCase(fqcn.substring(indexOfLastDot+1, fqcn.length()))
    return "/WEB-INF/jsp/renderer/$pkg/${className}.jsp"
  }

  protected def createResult(r) {
    if (!r) {
      r = [:]
    }
    r.fragmentPath = getFragmentPath()
    return r
  }

}
