package co.antonis.generator.gson.model;

/**
 * Contains two type of convert
 * <p>
 * Inline:          String to "Class"
 * Function:        Function<K,String.class>
 */
public class MethodConvert<C> {
    Class<C> clazz;

    /**
     * If true a replacement is needed with the name of the utilities class
     */
    public boolean isInUtilitiesClass = false;

    /**
     * Inline expression converter
     * <p>
     * Example:
     *      "Float.parseFloat($$$.toString())"
     *      "$$$.isString().stringValue() to jsonObject.get("string").isString().stringValue()"
     */
    public String inline_JsonV_to_Class;

    /**
     * Function<T,String>
     * Function<Integer,String> f =  (s) -> s.intValue();
     *
     * you can use it as f.apply(s)
     * <p>
     * Example "(s)-> s!=null ? Float.parseFloat(s): 0f"
     */
    public String func_String_to_Class;
    public String func_Class_to_String;

    /**
     * Function<T, JSONValue>
     * Function<Integer, JSONValue> f = (s) -> new JSONNumber(s)
     *
     */
    public String func_Class_to_JsonV;
    public String inline_Class_to_JsonV;

    /**
     *
     */

    public static <C> MethodConvert<C> i(Class<C> clazz, String inline_JsonV_ToClass, String func_String_ToClass) {
        return i(clazz, inline_JsonV_ToClass, null, func_String_ToClass, null, null);
    }

    public static <C> MethodConvert<C> i(Class<C> clazz,
                                         String inline_JsonV_to_Class,
                                         String inline_Class_toJsonV,
                                         String func_Class_to_JsonV,
                                         String func_String_to_Class,
                                         String func_Class_to_String) {
        return new MethodConvert<C>(clazz, inline_JsonV_to_Class, inline_Class_toJsonV, func_Class_to_JsonV, func_String_to_Class, func_Class_to_String,  false);
    }

    public MethodConvert(Class<C> clazz,
                         String inline_JsonV_ToClass,
                         String inline_Class_ToJsonV,
                         String func_Class_ToJsonV,
                         String func_String_ToClass,
                         String func_Class_ToString,
                         boolean isInUtilitiesClass) {
        this.clazz = clazz;
        this.inline_JsonV_to_Class = inline_JsonV_ToClass;
        this.inline_Class_to_JsonV = inline_Class_ToJsonV;
        this.func_Class_to_JsonV = func_Class_ToJsonV;

        this.func_String_to_Class = func_String_ToClass;
        this.func_Class_to_String = func_Class_ToString;

        this.isInUtilitiesClass = isInUtilitiesClass;
    }

    public String inline_JsonV_ToClass(String parameterName) {
        if (inline_JsonV_to_Class != null)
            return inline_JsonV_to_Class.replace("$$$", parameterName);
        return null;
    }

    public String inline_Class_ToJsonV(String parameterName) {
        if (inline_Class_to_JsonV != null)
            return inline_Class_to_JsonV.replace("$$$", parameterName);
        return null;
    }

    public MethodConvert<C> setInUtilitiesClass(boolean inUtilitiesClass) {
        isInUtilitiesClass = inUtilitiesClass;
        return this;
    }
}
