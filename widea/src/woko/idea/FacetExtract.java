/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.idea;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacetExtract {

    private static final Pattern fkSplitPattern = Pattern.compile("@FacetKey\\((.+?)\\)");
    private static final Pattern fkAttrsPattern = Pattern.compile("name=\"(.+?)\",profileId=\"(.+?)\"");

    public static List<WideaFacetDescriptor> createDescriptors(String facetKeysGroovyStr) {
        List<WideaFacetDescriptor> res = new ArrayList<WideaFacetDescriptor>();
        String s = facetKeysGroovyStr.replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\\s+", "")
                .replaceAll("\n", "");

        Matcher m = fkSplitPattern.matcher(s);
        while (m.find()) {
            String group = m.group(1);
            Matcher m2 = fkAttrsPattern.matcher(group);
            while (m2.find()) {
                String name = m2.group(1);
                String profileId = m2.group(2);
                System.out.println("name=" + name + ",profile=" + profileId);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        String s = "[\n" +
                "            @FacetKey(name = \"tataz\", profileId=\"zob\", targetObjectType =),\n" +
                "            @FacetKey(name = \"tataz\", profileId=\"lamouche\")\n" +
                "    ]";
        System.out.println(createDescriptors(s));
    }

}
