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

package woko.util;

import woko.Woko;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.Link;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LinkUtil {

    public static String getUrl(Woko<?,?,?,?> woko, Object o, String facetName) {
        ObjectStore s = woko.getObjectStore();
        String className = s.getClassMapping(o.getClass());
        String key = s.getKey(o);
        facetName = facetName == null ? WokoFacets.view : facetName;
        return facetName + "/" + className + "/" + key;
    }

    private static void appendAttr(StringBuilder sb, String name, String val) {
        if (val!=null) {
            sb.append(name).append("=").append("\"").append(val).append("\"");
        }
    }

    public static String computeAllLinkAttributes(Link l, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String href = l.getHref();
        if (href!=null) {
            sb.append(" ");
            href = request.getContextPath() + "/" + href;
            appendAttr(sb, "href", href);
        }
        String cssClass = l.getCssClass();
        if (cssClass!=null) {
            sb.append(" ");
            appendAttr(sb, "class", cssClass);
        }
        Map<String,String> attributes = l.getAttributes();
        Set<String> keys = attributes.keySet();
        if (keys.size()>0) {
            sb.append(" ");
        }
        for (Iterator<String> it = keys.iterator() ; it.hasNext() ; ) {
            String attrName = it.next();
            String attrVal = attributes.get(attrName);
            sb.append(attrName)
                    .append("=")
                    .append("\"")
                    .append(attrVal)
                    .append("\"");
            if (it.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
