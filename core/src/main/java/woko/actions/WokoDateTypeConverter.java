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

package woko.actions;

import net.sourceforge.stripes.validation.DateTypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import woko.facets.builtin.all.RenderPropertyValueJsonDate;

import java.util.Collection;
import java.util.Date;

public class WokoDateTypeConverter extends DateTypeConverter {

    @Override
    public Date convert(String input, Class<? extends Date> targetType, Collection<ValidationError> errors) {
        if (RenderPropertyValueJsonDate.isJsonDate(input)) {
            // RPC-enabled date : convert using time value
            return RenderPropertyValueJsonDate.dateFromJsonString(input);
        }
        return super.convert(input, targetType, errors);
    }
}
