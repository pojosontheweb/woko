package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@FacetKey(name="renderPropertyValueJson", profileId="all", targetObjectType=Date.class)
public class RenderPropertyValueJsonDate extends BaseFacet implements RenderPropertyValueJson {

    public static final String JSON_PREFIX = "/Date(";
    public static final String JSON_SUFFIX = ")/";

    private static final int PREFIX_LEN = JSON_PREFIX.length();
    private static final int SUFFIX_LEN = JSON_SUFFIX.length();

    @Override
    public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
        if (propertyValue==null) {
            return null;
        }
        return dateToJsonString((Date) propertyValue);
    }

    // http://stackoverflow.com/questions/206384/how-to-format-a-json-date
    public static String dateToJsonString(Date d) {
        return new StringBuilder().
          append(JSON_PREFIX).
          append(Long.toString(d.getTime())).
          append(JSON_SUFFIX).
          toString();
    }

    public static Date dateFromJsonString(String s) {
        if (s==null)
            return null;

        if (isJsonDate(s)) {
            String timeStr = s.substring(PREFIX_LEN, s.length() - SUFFIX_LEN);
            try {
                long time = Long.parseLong(timeStr);
                return new Date(time);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("supplied string ain't a valid JSON String : " + s, e);
            }
        }
        throw new IllegalArgumentException("supplied string ain't a valid JSON String : " + s);
    }

    public static boolean isJsonDate(String s) {
        return s.startsWith(JSON_PREFIX) && s.endsWith(JSON_SUFFIX);
    }
}
