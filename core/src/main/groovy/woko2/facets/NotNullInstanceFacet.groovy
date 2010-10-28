package woko2.facets

import net.sourceforge.jfacets.IInstanceFacet

class NotNullInstanceFacet implements IInstanceFacet {

  boolean matchesTargetObject(Object targetObject) {
    return targetObject != null
  }


}
