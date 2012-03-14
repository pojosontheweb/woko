/*
 * Copyright 2001-2010 Remi Vankeisbelck
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
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.localization.LocalePicker;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.builtin.RenderObjectJson;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@FacetKey(name= RenderObjectJson.FACET_NAME,profileId = "all", targetObjectType = ValidationErrors.class)
public class RenderValidationErrorsJson extends RenderObjectJsonImpl {

    @Override
    public JSONObject objectToJson(HttpServletRequest request) {
        ValidationErrors ve = (ValidationErrors)getFacetContext().getTargetObject();
        if (ve==null) {
            return null;
        }
        LocalePicker localePicker = StripesFilter.getConfiguration().getLocalePicker();
        Locale locale = localePicker.pickLocale(request);
        JSONObject res = new JSONObject();
        try {
            res.put("error", true);
            res.put("message", "Validation errors");
            res.put("validation", true);
            for (String field : ve.keySet()) {
                List<ValidationError> errs = ve.get(field);
                JSONArray jsonErrs = new JSONArray();
                    res.put(field,jsonErrs);
                for (ValidationError e : errs) {
                    JSONObject je = new JSONObject();
                    je.put("fieldName", e.getFieldName());
                    je.put("fieldValue", e.getFieldValue());
                    je.put("message", e.getMessage(locale));
                    jsonErrs.put(je);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
