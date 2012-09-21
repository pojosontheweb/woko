package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.ext.usermanagement.core.RegistrationDetails
import woko.ext.usermanagement.facets.registration.ViewRegistrationGuest

// neededbecause otherwise we fallback to the login required facet
// that is assigned to (view,guest,Object).
// in an app that uses its own guest role (which it should), this would
// not be needed
@FacetKey(name="view",profileId="guest", targetObjectType=RegistrationDetails.class)
class ViewHbRegistration extends ViewRegistrationGuest {
}
