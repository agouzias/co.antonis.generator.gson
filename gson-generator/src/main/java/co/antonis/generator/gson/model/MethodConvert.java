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
    public String inlineFromStringToClass;

    /**
     * A Lamda expression of
     * Function<Integer,String> f =  (s) -> s.intValue();
     * you can use it as f.apply(s)
     */
    public String funcFromStringToClass;

    //Not used
    public String funcFromJsonVToClass;
    public String inlineFromJsonVToClass;

    public static <C> MethodConvert<C> i(Class<C> clazz, String inline, String functionI) {
        return new MethodConvert<C>(clazz, inline, functionI);
    }

    public MethodConvert(Class<C> clazz, String inline, String functionIf) {
        this.clazz = clazz;
        this.inlineFromStringToClass = inline;
        this.funcFromStringToClass = functionIf;
    }

    public String inline(String name) {
        if (inlineFromStringToClass != null)
            return inlineFromStringToClass.replace("$$$", name);
        return null;
    }
}
