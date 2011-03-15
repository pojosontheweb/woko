package woko.facets.builtin.gitfs

import woko.facets.BaseFacet
import com.rvkb.gitfs.UserInfo

abstract class GitStoreFacetBase extends BaseFacet {

  String getUsername() {
    return facetContext.woko.getUsername(facetContext.request)
  }

  UserInfo getUserInfo() {
    return new UserInfo(username, "nomail")
  }

}
