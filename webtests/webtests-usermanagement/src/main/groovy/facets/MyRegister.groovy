package facets

import woko.ext.usermanagement.facets.registration.Register
import net.sourceforge.jfacets.annotations.FacetKey
import woko.ext.usermanagement.core.AccountStatus

@FacetKey(name='register',profileId='guest')
class MyRegister extends Register {

    @Override
    protected String getAppUrl() {
        return "http://localhost:8080/woko-webtests"
    }

    @Override
    protected AccountStatus getRegisteredAccountStatus() {
        AccountStatus.Registered
    }

    @Override
    protected List<String> getRegisteredUserRoles() {
        ["developer"]
    }


}
