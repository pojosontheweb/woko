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

/**
 * <code>renderPropertyValueEdit</code> for properties of type <code>Date</code> : input field
 * with date formatting and calendar widget.
 */
public class RenderPropertyValueEditMail extends RenderPropertyValueEditInput{

    private final String type="email";
    private final String title = "example: foo@pojosontheweb.com";

    @Override
    public String getType() {
       return this.type;
    }

    @Override
    public HashMap<String, String> getAttributes() {
       HashMap<String,String> tMap = new HashMap<String, String>();
        tMap.put("title", title);
        tMap.put("class", "")
        return tMap;
    }
}
