package woko.facets;

import net.sourceforge.jfacets.IInstanceFacet;

public class NotNullInstanceFacet implements IInstanceFacet {

  public boolean matchesTargetObject(Object targetObject) {
    return targetObject != null;
  }


}
