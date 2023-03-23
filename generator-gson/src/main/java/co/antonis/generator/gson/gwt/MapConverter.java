package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.model.MethodConvert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static co.antonis.generator.gson.gwt.CodeGenerator.classUtilities;

public class MapConverter {

    public static MethodConvert<Object> MethodConvert_ofPojo = MethodConvert.i(Object.class, "$$$.isObject().toString()", "(s)-> s!=null ? $$$ : ''");

    public static Map<Class<?>, MethodConvert<?>> MapConverters = new HashMap<>();

    /*
     * Converter of Primitive Types (String to Java Object)
     * inline OR method based (needed in list/map)
     */
    static {
        /*
         * Supported Primitive Types and convert function (inline and methods - for loops and maps)
         * Note that 'inline' (str->value) don't do 'null' since we check that jsonObject['field'] is not null before usage.
         * @see isCheckNotNullTheJsonValue_BeforeSetJavaField
         *
         * BUT the function (str) {value} it is used in containers and items should be added even if there are null
         */
        MapConverters.put(String.class,
                MethodConvert.i(String.class,
                        "$$$.isString().stringValue()",
                        "new JSONString($$$)",
                        "(str) -> (str!=null) ? new JSONString(str) : null",
                        "(s) -> s",
                        "(s) -> s"
                )); /*not use s.toString() in function to avoid null pointer*/

        MapConverters.put(Date.class,
                MethodConvert.i(Date.class,
                                classUtilities.simpleName() + ".toDateFromV($$$)",
                                classUtilities.simpleName() + ".toVFromDate($$$)",
                                "(pojo) -> " + classUtilities.simpleName() + ".toVFromDate(pojo)",
                                "(s) -> " + classUtilities.simpleName() + ".toDateFromS(s)",
                                "(dt) -> dt!=null ? Long.toString(dt.getTime()) : null"
                        )
                        .setInUtilitiesClass(true)
        );

        MapConverters.put(Integer.class,
                MethodConvert.i(Integer.class,
                        "Integer.parseInt($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> num!=null ? new JSONNumber(num) : null",
                        "(s) -> s!=null ? Integer.parseInt(s) : null",
                        "(num) -> num!=null ? Integer.toString(num) : null"
                ));

        MapConverters.put(int.class,
                MethodConvert.i(int.class,
                        "Integer.parseInt($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> new JSONNumber(num)",
                        "(s) -> s!=null ? Integer.parseInt(s) : 0",
                        "(num) -> Integer.toString(num)"
                ));

        MapConverters.put(Long.class,
                MethodConvert.i(Long.class,
                        "Long.parseLong($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> num!=null ? new JSONNumber(num) : null",
                        "(s) -> s!=null ?Long.parseLong(s) : null",
                        "(num) -> num!=null ? Long.toString(num) : null"
                ));

        MapConverters.put(long.class,
                MethodConvert.i(long.class,
                        "Long.parseLong($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> new JSONNumber(num)",
                        "(s) -> s!=null ? Long.parseLong(s) : 0",
                        "(num) -> Long.toString(num)"
                ));

        MapConverters.put(Double.class,
                MethodConvert.i(Double.class,
                        "Double.parseDouble($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> num!=null ? new JSONNumber(num) : null",
                        "(s) -> s!=null ? Double.parseDouble(s) : null",
                        "(num) -> num!=null ? Double.toString(num) : null"

                ));

        MapConverters.put(double.class,
                MethodConvert.i(double.class,
                        "Double.parseDouble($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> new JSONNumber(num)",
                        "(s) -> s!=null ? Double.parseDouble(s): null",
                        "(num) -> Double.toString(num)"
                ));

        MapConverters.put(Float.class,
                MethodConvert.i(Float.class,
                        "Float.parseFloat($$$.toString())",
                        "new JSONNumber($$$)",
                        "(num) -> num!=null ? new JSONNumber(num) : null",
                        "(s) -> s!=null ?Float.parseFloat(s): null",
                        "(num) -> num!=null ? Float.toString(num) : null"
                ));

        MapConverters.put(float.class,
                MethodConvert.i(float.class,
                        "Float.parseFloat($$$.toString())",
                        "new JSONNumber($$$)",
                        "(pojo) -> new JSONNumber(pojo)",
                        "(s) -> s!=null ?Float.parseFloat(s): 0f",
                        "(num) -> Float.toString(num)"
                ));

        MapConverters.put(Boolean.class,
                MethodConvert.i(Boolean.class,
                        "Boolean.parseBoolean($$$.toString())",
                        "JSONBoolean.getInstance($$$)",
                        "(b) -> JSONBoolean.getInstance(b)",
                        "(s) -> s!=null ? Boolean.parseBoolean(s): null",
                        "(b) -> b!=null ? Boolean.toString(b) : null"
                ));

        MapConverters.put(boolean.class,
                MethodConvert.i(boolean.class,
                        "Boolean.parseBoolean($$$.toString())",
                        "JSONBoolean.getInstance($$$)",
                        "(pojo) -> JSONBoolean.getInstance(pojo)",
                        "(s) -> s!=null ? Boolean.parseBoolean(s) : false",
                        "(b) -> Boolean.toString(b)"
                ));

        MapConverters.put(Character.class,
                MethodConvert.i(Character.class,
                        "$$$.isString().stringValue().charAt(0)",
                        "(s)->s!=null ? Character.valueOf(s): null"
                ));

        MapConverters.put(char.class,
                MethodConvert.i(char.class,
                        "$$$.isString().stringValue().charAt(0)",
                        "(s)-> s!=null ? Character.valueOf(s) : ''"
                ));

        //Special Case / Special Handling
    }
}
