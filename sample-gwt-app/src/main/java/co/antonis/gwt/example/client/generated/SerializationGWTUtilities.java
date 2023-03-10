package co.antonis.gwt.example.client.generated;


import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.*;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class SerializationGWTUtilities {
    public static Logger log = Logger.getLogger("");

    public static DateTimeFormat dateFormat_ISO8601 = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static DateTimeFormat dateFormat_Original = DateTimeFormat.getFormat("MMM, d, yyyy hh:mm:ss a");

    //region Primitives
    public static boolean toBoolean(JSONValue value) {
        if (value != null) {
            JSONBoolean bool = value.isBoolean();
            if (bool != null)
                return bool.booleanValue();
        }
        return false;
    }

    public static String toString(JSONValue value) {
        if (value != null)
            return value.toString();
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

    //region Date Converter
    public static Date toDateFromV(JSONValue jsonDate) {
        if (jsonDate == null)
            return null;
        if (jsonDate.isNumber() != null)
            try {
                return new java.util.Date((long) jsonDate.isNumber().doubleValue());
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
            return new java.util.Date(Long.parseLong(jsonDate));
        } catch (Exception ignored) {
        }
        try {
            return dateFormat_ISO8601.parse(jsonDate);
        } catch (Exception ignored) {
        }
        return dateFormat_Original.parse(jsonDate);
    }
    //endregion

    //region List
    public static <T> List<T> toListString_FuncS(String jsonS, Function<String, T> convert) {
        if (jsonS == null)
            return null;
        return toListJson_FuncS(JSONParser.parseStrict(jsonS), convert);
    }

    public static <T> List<T> toListJson_FuncS(JSONValue jsonV, Function<String, T> convert) {
        if (jsonV != null) {
            List<T> listStructure = new ArrayList<>();
            JSONArray jsonArray = jsonV.isArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                //log.info("Item " + i + ":" + jsonArray.get(i) + " [ is null " + isNull(jsonArray.get(i)) + "][");
                listStructure.add(convert.apply(toStringJSONValue(jsonArray.get(i))));
            }
            return listStructure;
        }
        return null;
    }

    public static <T> List<T> toListJson_FuncJ(JSONValue jsonV, Function<JSONValue, T> convert) {
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
    //endregion

    //region Methods Map
    public static <K, V> Map<K, V> toMapString_FuncS(String jsonS, Function<String, K> convertKey, Function<String, V> convertValue) {
        if (jsonS == null)
            return null;
        return toMapJson_FuncS(JSONParser.parseStrict(jsonS), convertKey, convertValue);
    }

    public static <K, V> Map<K, V> toMapJson_FuncS(JSONValue jsonV, Function<String, K> convertKey, Function<String, V> convertValue) {
        if (jsonV != null) {
            Map<K, V> mapStructure = new HashMap<>();
            JSONObject jsonObject = jsonV.isObject();
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                //TODO Maybe there is unneeded check (if the value is null then probably it is not 'set' in the map, so no need to check for not null value)
                mapStructure.put(convertKey.apply(key), convertValue.apply(toStringJSONValue(jsonObject.get(key))));
            }
            return mapStructure;
        }
        return null;
    }

    public static <K, V> Map<K, V> toMapJson_FuncJ(JSONValue jsonV, Function<String, K> convertKey, Function<JSONValue, V> convertValue) {
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
    //endregion

    /**
     * Wrong Way:
     * !isNull(v) ? v.toString() : null;
     * <p>
     * Correct Way :The "string" json value is quoted. the proper way is to use
     * !isNull(v) ? (v.isString() != null ? v.isString().stringValue() : v.toString()) : null
     */
    public static String toStringJSONValue(JSONValue v) {
        return !isNull(v) ? (v.isString() != null ? v.isString().stringValue() : v.toString()) : null;
    }


    public static boolean isNull(JSONValue obj) {
        return obj == null || obj.isNull() != null;
    }


}
