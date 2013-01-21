package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseFragmentFacet;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="renderRegisterProperties", profileId = "all")
public class RenderRegisterProperties<T extends User,
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,T>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "renderRegisterProperties";
    public static final String FRAGMENT_PATH = "/WEB-INF/woko/ext/usermanagement/renderRegisterProperties.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getReCaptchaPublicKey() {
        return null;
    }

    public String getReCaptchaPrivateKey() {
        return null;
    }
}
