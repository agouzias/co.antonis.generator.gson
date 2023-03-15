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
    public String inline_JsonV_ToClass;

    /**
     * A Lamda expression of
     * Function<Integer,String> f =  (s) -> s.intValue();
     * you can use it as f.apply(s)
     * <p>
     * Example "(s)-> s!=null ?Float.parseFloat(s): 0f"
     */
    public String func_String_ToClass;
    public String func_Class_ToString;

    //Not used
    public String funcFromJsonVToClass;

    /**
     *
     */
    public String inline_Class_ToJsonV;

    public static <C> MethodConvert<C> i(Class<C> clazz, String inline_JsonV_ToClass, String func_String_ToClass) {
        return i(clazz, inline_JsonV_ToClass, null, func_String_ToClass, null);
    }

    public static <C> MethodConvert<C> i(Class<C> clazz, String inline_JsonV_ToClass, String inline_Class_toJsonV, String func_String_ToClass, String func_Class_ToString) {
        return new MethodConvert<C>(clazz, inline_JsonV_ToClass, inline_Class_toJsonV, func_String_ToClass, func_Class_ToString, false);
    }

    public MethodConvert(Class<C> clazz,
                         String inline_JsonV_ToClass,
                         String inline_Class_ToJsonV,
                         String func_String_ToClass,
                         String func_Class_ToString,
                         boolean isInUtilitiesClass) {
        this.clazz = clazz;
        this.inline_JsonV_ToClass = inline_JsonV_ToClass;
        this.inline_Class_ToJsonV = inline_Class_ToJsonV;

        this.func_String_ToClass = func_String_ToClass;
        this.func_Class_ToString = func_Class_ToString;

        this.isInUtilitiesClass = isInUtilitiesClass;
    }

    public String inline_JsonV_ToClass(String parameterName) {
        if (inline_JsonV_ToClass != null)
            return inline_JsonV_ToClass.replace("$$$", parameterName);
        return null;
    }

    public String inline_Class_ToJsonV(String parameterName) {
        if (inline_Class_ToJsonV != null)
            return inline_Class_ToJsonV.replace("$$$", parameterName);
        return null;
    }

    public MethodConvert setInUtilitiesClass(boolean inUtilitiesClass) {
        isInUtilitiesClass = inUtilitiesClass;
        return this;
    }
}
