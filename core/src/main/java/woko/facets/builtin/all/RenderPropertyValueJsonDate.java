package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@FacetKey(name="renderPropertyValueJson", profileId="all", targetObjectType=Date.class)
public class RenderPropertyValueJsonDate extends BaseFacet implements RenderPropertyValueJson {

    @Override
    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        if (propertyValue==null) {
            return null;
        }
        Date d = (Date)propertyValue;
        // http://stackoverflow.com/questions/206384/how-to-format-a-json-date
        return "/Date(" + d.getTime() + ")/";
    }
}
