package co.antonis.gwt.example.client.generated;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.*;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Part of the CodeGeneration Library, needed to be copied in the same package with the
 * generated code (from/to) pojo converters.
 * <p>
 * Holding helper methods.
 * <p>
 * to exclude
 * com.vms.daq.common.structure.form.impl.*
 * com.vms.daq.common.structure.form.FormParameterValue
 * com.vms.daq.common.structure.constant.*
 */
public class SerGwtUtils {
    public static Logger log = Logger.getLogger("");

    public static DateTimeFormat dateFormat_ISO8601 = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static DateTimeFormat dateFormat_Original = DateTimeFormat.getFormat("MMM, d, yyyy hh:mm:ss a");

    //region Testing from JSON
    //endregion

    //region Testing to JSON from Primitive
    public static String testingToJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("num", new JSONNumber(3.4));
        jsonObject.put("key", new JSONString("str"));
        jsonObject.put("bool", JSONBoolean.getInstance(true));
        return jsonObject.toString();
    }
    //endregion

    //region Methods from JSON to Primitives --Not used in Generator-- converting FROM string using inline approach
    public static boolean toBoolean(JSONValue value) {
        if (value != null) {
            JSONBoolean bool = value.isBoolean();
            if (bool != null)
                return bool.booleanValue();
        }
        return false;
    }

    /**
     * The correct way to make it JSON jsonObject.get("dataObject").isObject()
     */
    public static String toString_Basic(JSONValue value) {
        if (value != null)
            return value.toString();
        return null;
    }

    /**
     * Wrong Way:
     * !isNull(v) ? v.toString() : null;
     * <p>
     * Correct Way :The "string" json value is quoted. the proper way is to use
     * !isNull(v) ? (v.isString() != null ? v.isString().stringValue() : v.toString()) : null
     */
    public static String toString_Extended(JSONValue v) {
        return !isNull(v) ? (v.isString() != null ? v.isString().stringValue() : v.toString()) : null;
    }

    public static String toString_RemoveQuotes(String v) {
        if (v != null)
            return v.replaceAll("\"", "");
        return null;
    }

    public static boolean isNull(JSONValue obj) {
        return obj == null || obj.isNull() != null;
    }


    //TODO check if needed
    public static JSONValue fromString(String s) {
        //vs return just JSONString(s)
        if (s != null)
            return new JSONString(s);
        return null;
    }

    public static Double toDouble(JSONValue value) {
        if (value != null) {
            JSONNumber number = value.isNumber();
            if (number != null)
                return number.doubleValue();
        }
        return null;
    }

    public static Integer toInt(JSONValue value) {
        Double number = toDouble(value);
        return number != null ? number.intValue() : null;
    }

    public static Long toLong(JSONValue value) {
        Double number = toDouble(value);
        return number != null ? number.longValue() : null;
    }
    //endregion

    //region Methods to/from JSON to Date Converter
    public static Date toDateFromV(JSONValue jsonDate) {
        if (jsonDate == null)
            return null;
        if (jsonDate.isNumber() != null)
            try {
                return new Date((long) jsonDate.isNumber().doubleValue());
            } catch (Exception ignored) {
            }
        if (jsonDate.isString() != null) {
            try {
                return dateFormat_ISO8601.parse(jsonDate.isString().stringValue());
            } catch (Exception ignored) {
            }
            return dateFormat_Original.parse(jsonDate.isString().stringValue());
        }
        throw new RuntimeException("Unable to convert date from json " + jsonDate);
    }

    public static Date toDateFromS(String jsonDate) {
        if (jsonDate == null)
            return null;
        //the toString() add quotes
        jsonDate = jsonDate.replaceAll("\"", "");
        try {
            return new Date(Long.parseLong(jsonDate));
        } catch (Exception ignored) {
        }
        try {
            return dateFormat_ISO8601.parse(jsonDate);
        } catch (Exception ignored) {
        }
        return dateFormat_Original.parse(jsonDate);
    }

    public static JSONNumber toVFromDate(Date date) {
        if (date == null)
            return null;
        return new JSONNumber(date.getTime());
    }
    //endregion

    //region Methods Classic Containers (String/Number) --Not used in Generator--
    public static List<String> toListString(String json) {
        return toListPojo_String_FuncS(json, s -> s);
    }

    public static JSONValue fromListString(List<String> list) {
        return toListJson_FuncJ(list, s-> s!=null ? new JSONString(s) : null);
    }

    public static List<Long> toListLong(String json) {
        return toListPojo_String_FuncS(json, Long::parseLong);
    }

    public static Map<String, String> toMapString(String json) {
        return toMapPojo_String_FuncS(json, s -> s, s -> s);
    }

    //endregion

    //region Methods to/from List
    public static <T> List<T> toListPojo_String_FuncS(String jsonS, Function<String, T> convert) {
        if (jsonS == null)
            return null;
        return toListPojo_JsonV_FuncS(JSONParser.parseStrict(jsonS), convert);
    }

    public static <T> List<T> toListPojo_JsonV_FuncS(JSONValue jsonV, Function<String, T> convert) {
        if (jsonV != null) {
            List<T> listStructure = new ArrayList<>();
            JSONArray jsonArray = jsonV.isArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                listStructure.add(convert.apply(toString_Extended(jsonArray.get(i))));
            }
            return listStructure;
        }
        return null;
    }

    public static <T> List<T> toListPojo_JsonV_FuncJ(JSONValue jsonV, Function<JSONValue, T> convert) {
        if (jsonV != null) {
            List<T> listStructure = new ArrayList<>();
            JSONArray jsonArray = jsonV.isArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                listStructure.add(convert.apply(jsonArray.get(i)));
            }
            return listStructure;
        }
        return null;
    }

    public static <T> JSONArray toListJson_FuncJ(List<T> list, Function<T, JSONValue> convert) {
        if (list != null) {
            JSONArray jsonArray = new JSONArray();
            for (int index = 0; index < list.size(); index++) {
                jsonArray.set(index, convert.apply(list.get(index)));
            }
            return jsonArray;
        }
        return null;
    }
    //endregion

    //region Methods to/from Map
    public static <K, V> Map<K, V> toMapPojo_String_FuncS(String jsonS, Function<String, K> convertKey, Function<String, V> convertValue) {
        if (jsonS == null)
            return null;
        return toMapPojo_JsonV_FuncS(JSONParser.parseStrict(jsonS), convertKey, convertValue);
    }

    public static <K, V> Map<K, V> toMapPojo_JsonV_FuncS(JSONValue jsonV, Function<String, K> convertKey, Function<String, V> convertValue) {
        if (jsonV != null) {
            Map<K, V> mapStructure = new HashMap<>();
            JSONObject jsonObject = jsonV.isObject();
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                //TODO Maybe there is unneeded check (if the value is null then probably it is not 'set' in the map, so no need to check for not null value)
                mapStructure.put(convertKey.apply(key), convertValue.apply(toString_Extended(jsonObject.get(key))));
            }
            return mapStructure;
        }
        return null;
    }

    public static <K, V> Map<K, V> toMapPojo_JsonV_FuncJ(JSONValue jsonV, Function<String, K> convertKey, Function<JSONValue, V> convertValue) {
        if (jsonV != null) {
            Map<K, V> mapStructure = new HashMap<>();
            JSONObject jsonObject = jsonV.isObject();
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                mapStructure.put(convertKey.apply(key), convertValue.apply(jsonObject.get(key)));
            }
            return mapStructure;
        }
        return null;
    }

    public static <K, V> JSONObject toMapJson_FuncJ(Map<K, V> map,
                                                    Function<K, JSONValue> convertKey,
                                                    Function<V, JSONValue> convertValue) {
        if (map != null) {
            JSONObject jsonMap = new JSONObject();
            map.keySet().forEach(key -> {
                //JSONValue jsonV =convertKey.apply(key);
                //String keyV = jsonV.isString()!=null ? jsonV.isString().toString() : jsonV.toString();
                jsonMap.put(
                        toString_Extended(convertKey.apply(key)),
                        convertValue.apply(map.get(key))
                );
            });
            return jsonMap;
        }
        return null;
    }

    public static <K, V> JSONObject toMapJson_FuncJ_FuncS(Map<K, V> map,
                                                          Function<K, String> convertKey,
                                                          Function<V, JSONValue> convertValue) {
        if (map != null) {
            JSONObject jsonMap = new JSONObject();
            map.keySet().forEach(key -> {
                jsonMap.put(
                        convertKey.apply(key),
                        convertValue.apply(map.get(key))
                );
            });
            return jsonMap;
        }
        return null;
    }
    //endregion


}

