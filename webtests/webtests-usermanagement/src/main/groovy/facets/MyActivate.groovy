package facets

import woko.ext.usermanagement.facets.registration.ActivateGuest
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="activate", profileId="guest")
class MyActivate extends ActivateGuest {

}
