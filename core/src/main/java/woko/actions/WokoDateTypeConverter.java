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
