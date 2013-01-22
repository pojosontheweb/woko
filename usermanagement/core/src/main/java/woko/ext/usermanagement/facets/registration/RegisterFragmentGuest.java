package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseFragmentFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="registerFragment", profileId = "all")
public class RegisterFragmentGuest<T extends User,
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,T>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "registerFragment";
    public static final String FRAGMENT_PATH = "/WEB-INF/woko/ext/usermanagement/registerFragment.jsp";

    private RegisterGuest registerFacet;

    private T user;

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public String getReCaptchaPublicKey() {
        return getRegisterFacet().getReCaptchaPublicKey();
    }

    public String getReCaptchaPrivateKey() {
        return getRegisterFacet().getReCaptchaPrivateKey();
    }

    public boolean isUseCaptcha() {
        return getRegisterFacet().isUseCaptcha();
    }

    public User getUser(){
        return getRegisterFacet().getUser();
    }

    public RegisterGuest getRegisterFacet(){
        if (registerFacet==null)
            registerFacet = getWoko().getFacet("register", getRequest(), null);
        return registerFacet;
    }
}
