package woko.facets;

import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;
import woko.users.UserManager;

import java.util.ArrayList;
import java.util.List;

public class WokoProfileRepository implements IProfileRepository {

  private final UserManager userManager;

  public WokoProfileRepository(UserManager userManager) {
    this.userManager = userManager;
  }

  public IProfile getProfileById(String profileId) {
    return new WokoProfile(profileId);
  }

  public IProfile[] getSuperProfiles(IProfile profile) {
    List<String> roles = userManager.getRoles(profile.getId());
    List<IProfile> profiles = new ArrayList<IProfile>();
    for (String role : roles) {
      profiles.add(new WokoProfile(role));
    }
    IProfile[] result = new IProfile[profiles.size()];
    return profiles.toArray(result);
  }

}
