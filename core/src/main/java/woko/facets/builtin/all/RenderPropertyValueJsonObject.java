package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

@FacetKey(name = WokoFacets.renderPropertyValueJson, profileId = "all")
public class RenderPropertyValueJsonObject extends BaseFacet implements RenderPropertyValueJson {

    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        if (propertyValue == null) {
            return null;
        }
        JSONObject res = new JSONObject();
        RenderObjectJsonImpl.addWokoMetadata(getFacetContext().getWoko(), res, propertyValue, request);
        return res;
    }

}
