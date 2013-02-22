package woko.actions;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

@UrlBinding("/theme/{name}")
public class SwithThemeActionBean extends BaseActionBean {

    public static final String THEME_COOKIE = "themeName";

    @Validate(required=true)
    public String name;

    @DefaultHandler
    public Resolution execute() {
        getContext().getRequest().getSession().setAttribute(THEME_COOKIE, name);
        getContext().getMessages().add(new SimpleMessage("Theme changed. Like it better ?"));
        return new RedirectResolution("/themeRoller");
    }

}
