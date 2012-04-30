/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=Date.class)
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
