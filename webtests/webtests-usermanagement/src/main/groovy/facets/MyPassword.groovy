package facets

import woko.ext.usermanagement.facets.password.Password
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="password", profileId="all")
class MyPassword extends Password {

    @Override
    protected String getFromEmailAddress() {
        "fake@wokoland.com"
    }

    @Override
    protected String getAppUrl() {
        "http://localhost:8080/woko-webtests"
    }
}
