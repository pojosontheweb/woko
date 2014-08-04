package webtests.usermanagement.facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import test.MyUser
import woko.ext.usermanagement.core.User
import woko.ext.usermanagement.facets.registration.RegisterGuest

@FacetKey(name = RegisterGuest.FACET_NAME, profileId = "all")
class MyRegister extends RegisterGuest {

    @Override
    protected void beforeValidate(User user) {
        MyUser mu = (MyUser)user
        mu.prop1 = "I can't be null"
    }

    @Override
    boolean isUseCaptcha() {
        boolean res = Boolean.valueOf(System.getProperty("woko.webtests.recaptcha", "false"))
        return res
    }

    @Override
    String getReCaptchaPrivateKey() {
        return System.getProperty("woko.webtests.recaptcha.private.key")
    }

    @Override
    String getReCaptchaPublicKey() {
        return System.getProperty("woko.webtests.recaptcha.public.key")
    }

}
