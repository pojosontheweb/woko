package facets

import woko.ext.usermanagement.facets.registration.Register
import net.sourceforge.jfacets.annotations.FacetKey
import woko.ext.usermanagement.core.AccountStatus

@FacetKey(name='register',profileId='guest')
class MyRegister extends Register {

    @Override
    protected List<String> getRegisteredUserRoles() {
        ["developer"]
    }

}
