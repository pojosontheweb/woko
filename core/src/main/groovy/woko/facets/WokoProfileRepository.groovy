package woko.facets

import net.sourceforge.jfacets.IProfileRepository
import net.sourceforge.jfacets.IProfile
import woko.users.UserManager

class WokoProfileRepository implements IProfileRepository {

  private final UserManager userManager

  WokoProfileRepository(UserManager userManager) {
    this.userManager = userManager;
  }

  IProfile getProfileById(String profileId) {
    return new WokoProfile(profileId)
  }

  IProfile[] getSuperProfiles(IProfile profile) {
    String id = profile.id
    def roles = userManager.getRoles(id)
    IProfile[] result = new IProfile[roles.size()]
    return roles.toArray(result)
  }

}
