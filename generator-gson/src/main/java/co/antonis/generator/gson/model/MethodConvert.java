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
     * Inline expression converter
     */
    public String inline_JsonV_ToClass;

    /**
     * A Lamda expression of
     * Function<Integer,String> f =  (s) -> s.intValue();
     * you can use it as f.apply(s)
     */
    public String func_String_ToClass;

    //Not used
    public String funcFromJsonVToClass;

    public static <C> MethodConvert<C> i(Class<C> clazz, String inline_JsonV_ToClass, String func_String_ToClass) {
        return new MethodConvert<C>(clazz, inline_JsonV_ToClass, func_String_ToClass);
    }

    public MethodConvert(Class<C> clazz, String inline_JsonV_ToClass, String func_String_ToClass) {
        this.clazz = clazz;
        this.inline_JsonV_ToClass = inline_JsonV_ToClass;
        this.func_String_ToClass = func_String_ToClass;
    }

    public String inline_JsonV_ToClass(String parameterName) {
        if (inline_JsonV_ToClass != null)
            return inline_JsonV_ToClass.replace("$$$", parameterName);
        return null;
    }
}
