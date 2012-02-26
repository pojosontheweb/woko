package woko.facets.builtin;

import woko.facets.builtin.all.*;
import woko.facets.builtin.all.HomeImpl;
import woko.facets.builtin.developer.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WokoFacets {

    public static final String create = "create";
    public static final String delete = Delete.FACET_NAME;
    public static final String edit = Edit.FACET_NAME;
    public static final String find = "find";
    public static final String home = Home.FACET_NAME;
    public static final String json = Json.FACET_NAME;
    public static final String layout = Layout.FACET_NAME;
    public static final String list = ListObjects.FACET_NAME;
    public static final String login = Login.FACET_NAME;
    public static final String logout = Logout.FACET_NAME;
    public static final String navBar = NavBar.FACET_NAME;
    public static final String renderLinks = RenderLinks.FACET_NAME;
    public static final String renderLinksEdit = RenderLinks.FACET_NAME + "Edit";
    public static final String renderObject = RenderObject.FACET_NAME;
    public static final String renderObjectEdit = RenderObject.FACET_NAME + "Edit";
    public static final String renderObjectJson = RenderObjectJson.FACET_NAME;
    public static final String renderProperties = RenderProperties.FACET_NAME;
    public static final String renderPropertiesEdit = RenderProperties.FACET_NAME + "Edit";
    public static final String renderPropertyName = RenderPropertyName.FACET_NAME;
    public static final String renderPropertyValue = RenderPropertyValue.FACET_NAME;
    public static final String renderPropertyValueEdit = RenderPropertyValueEdit.FACET_NAME;
    public static final String renderPropertyValueJson = RenderPropertyValueJson.FACET_NAME;
    public static final String renderTitle = RenderTitle.FACET_NAME;
    public static final String save = Save.FACET_NAME;
    public static final String search = Search.FACET_NAME;
    public static final String studio = "studio";
    public static final String validate = Validate.FACET_NAME;
    public static final String view = View.FACET_NAME;

    public static class IntfAndClass {

        private final Class<?> intf;
        private final Class<?> clazz;

        private IntfAndClass(Class<?> intf, Class<?> clazz) {
            this.intf = intf;
            this.clazz = clazz;
        }

        public Class<?> getIntf() {
            return intf;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }

    private static final Map<String,IntfAndClass> FACETS = Collections.unmodifiableMap(new HashMap<String, IntfAndClass>() {{
        put(delete, new IntfAndClass(Delete.class, DeleteImpl.class));
        put(edit, new IntfAndClass(Edit.class, EditImpl.class));
        put(home, new IntfAndClass(Home.class, HomeImpl.class));
        put(json, new IntfAndClass(Json.class, JsonImpl.class));
        put(layout, new IntfAndClass(Layout.class, LayoutAll.class));
        put(list, new IntfAndClass(ListObjects.class, ListImpl.class));
        put(navBar, new IntfAndClass(NavBar.class, NavBarAll.class));
        put(renderLinks, new IntfAndClass(RenderLinks.class, RenderLinksImpl.class));
        put(renderLinksEdit, new IntfAndClass(RenderLinks.class, RenderLinksEditImpl.class));
        put(renderObject, new IntfAndClass(RenderObject.class, RenderObjectImpl.class));
        put(renderObjectEdit, new IntfAndClass(RenderObject.class, RenderObjectEditImpl.class));
        put(renderObjectJson, new IntfAndClass(RenderObjectJson.class, RenderObjectJsonImpl.class));
        put(renderProperties, new IntfAndClass(RenderProperties.class, RenderPropertiesImpl.class));
        put(renderPropertiesEdit, new IntfAndClass(RenderProperties.class, RenderPropertiesEditImpl.class));
        put(renderPropertyName, new IntfAndClass(RenderPropertyName.class, RenderPropertyNameImpl.class));
        put(renderPropertyValue, new IntfAndClass(RenderPropertyValue.class, RenderPropertyValueImpl.class));
        put(renderPropertyValueEdit, new IntfAndClass(RenderPropertyValue.class, RenderPropertyValueEditStripesText.class));
        put(renderPropertyValueJson, new IntfAndClass(RenderPropertyValueJson.class, RenderPropertyValueJsonObject.class));
        put(renderTitle, new IntfAndClass(RenderTitle.class, RenderTitleImpl.class));
        put(save, new IntfAndClass(Save.class, SaveImpl.class));
        put(search, new IntfAndClass(Search.class, SearchImpl.class));
        put(validate, new IntfAndClass(Validate.class, null));
        put(view, new IntfAndClass(View.class, ViewImpl.class));
    }});

    public static Class<?> getInterface(String facetName) {
        IntfAndClass ic = FACETS.get(facetName);
        return ic==null ? null : ic.getIntf();
    }

    public static Class<?> getDefaultImpl(String facetName) {
        IntfAndClass ic = FACETS.get(facetName);
        return ic==null ? null : ic.getClazz();
    }

}
