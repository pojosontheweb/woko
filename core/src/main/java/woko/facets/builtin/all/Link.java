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

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for creating HTML hyperlinks or buttons.
 */
public class Link {

    private final String href;
    private final String text;
    private String cssClass;
    private final Map<String,String> attributes = new HashMap<String, String>();

    /**
     * Create the link with passed href and text
     * @param href the href
     * @param text the text
     */
    public Link(String href, String text) {
        this.href = href;
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public String getText() {
        return text;
    }

    public Link setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public String getCssClass() {
        return cssClass;
    }

    public Link addAttribute(String name, String value) {
        attributes.put(name,value);
        return this;
    }

    public Map<String,String> getAttributes() {
        return attributes;
    }
}
