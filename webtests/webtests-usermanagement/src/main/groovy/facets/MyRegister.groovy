package facets

import woko.ext.usermanagement.facets.registration.Register
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='register',profileId='guest')
class MyRegister extends Register {

    @Override
    protected String getAppUrl() {
        return "http://localhost:8080/woko-webtests"
    }


}
