package woko2.facets

import net.sourceforge.jfacets.IProfile

class WokoProfile implements IProfile {

  private final String id

  WokoProfile(String id) {
    this.id = id;
  }

  String getId() {
    return id
  }
}
