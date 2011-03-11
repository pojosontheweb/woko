package woko.facets;

import net.sourceforge.jfacets.IProfile;

public class WokoProfile implements IProfile {

  private final String id;

  public WokoProfile(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
