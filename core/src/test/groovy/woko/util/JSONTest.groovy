package woko.util

import org.junit.Test

class JSONTest {

    private static void tripObject(Map o) {
        assert o == JSON.toMap(JSON.toJSONObject(o))
    }

    private static void tripArray(List o) {
        assert o == JSON.toList(JSON.toJSONArray(o))
    }

    @Test
    void testJSON() {
        tripObject([
            foo:123
        ])
        tripObject([
            foo:'bar',
            bar:123,
            baz:true
        ])
        tripObject([
            foo:'bar',
            bar:[
                baz:123
            ]
        ])
        tripObject([
            foo:'bar',
            bar:[
                baz:123,
                list:[1,2,3]
            ]
        ])
        tripArray([1,2,3])
        tripArray([1,[foo:123],[true, "yeah"]])
    }

}
