package woko.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JSON {

    private JSON() {}

    public static JSONObject toJSONObject(Map<String,?> map) {
        if (map==null) {
            return null;
        }
        JSONObject result = new JSONObject();
        try {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value!=null) {
                    result.put(key, toJSONValue(map.get(key)));
                }
            }
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static boolean isPrimitive(Object o) {
        return o instanceof Boolean ||
            o instanceof String ||
            o instanceof Number;
    }

    private static Object toJSONValue(Object value) {
        if (isPrimitive(value)) {
            return value;
        } else if (value instanceof List) {
            return toJSONArray((List<?>)value);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String,?> m = (Map<String,?>)value;
            return toJSONObject(m);
        } else {
            throw new IllegalStateException("Unhandled value type " + value);
        }
    }

    public static JSONArray toJSONArray(List<?> list) {
        if (list==null) {
            return null;
        }
        JSONArray result = new JSONArray();
        for (Object o : list) {
            result.put(toJSONValue(o));
        }
        return result;
    }

    public static Map<String,?> toMap(JSONObject jsonObject) {
        if (jsonObject==null) {
            return null;
        }
        Map<String,Object> result = new HashMap<String,Object>();
        Iterator<?> keys = jsonObject.keys();
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = jsonObject.get(key);
                if (value!=null) {
                    result.put(key, fromJSONValue(value));
                }
            }
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static Object fromJSONValue(Object jsonValue) {
        if (jsonValue==null) {
            return null;
        }
        if (isPrimitive(jsonValue)) {
            return jsonValue;
        } else {
            if (jsonValue instanceof JSONObject) {
                return toMap((JSONObject)jsonValue);
            } else if (jsonValue instanceof JSONArray) {
                return toList((JSONArray)jsonValue);
            } else {
                throw new IllegalStateException("Unhandled json value type " + jsonValue);
            }
        }
    }

    public static List<?> toList(JSONArray jsonArray) {
        if (jsonArray==null) {
            return null;
        }
        List<Object> result = new ArrayList<Object>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                result.add(fromJSONValue(jsonArray.get(i)));
            }
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
