package webtests.usermanagement.facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.ext.usermanagement.facets.registration.Register

@FacetKey(name = Register.FACET_NAME, profileId = "all")
class MyRegister extends Register {

    @Override
    boolean isUseCaptcha() {
        Boolean.valueOf(System.getProperty("woko.webtests.recaptcha", "false"))
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
