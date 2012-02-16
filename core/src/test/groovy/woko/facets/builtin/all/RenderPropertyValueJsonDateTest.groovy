package woko.facets.builtin.all

import org.junit.Test
import static RenderPropertyValueJsonDate.*

class RenderPropertyValueJsonDateTest {

    @Test
    void testDateToJsonString() {
        Date d = new Date()
        String s = dateToJsonString(d)
        assert isJsonDate(s)
    }

    @Test
    void testDateFromJsonString() {
        Date d1 = new Date()
        String s = "/Date(${d1.time})/"
        assert isJsonDate(s)
        Date d2 = dateFromJsonString(s)
        assert d1.equals(d2)
    }

}
