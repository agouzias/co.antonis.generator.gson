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
                        "(s)->s",
                        "(s)->s"
                )); /*not use s.toString() in function to avoid null pointer*/

        MapConverters.put(Date.class,
                MethodConvert.i(Date.class,
                                classUtilities.simpleName() + ".toDateFromV($$$/*.isString().stringValue()*/)",
                                classUtilities.simpleName() + ".toVFromDate($$$)",
                                "(s)->" + classUtilities.simpleName() + ".toDateFromS(s)",
                                "TODO Date"
                        )
                        .setInUtilitiesClass(true)
        );

        MapConverters.put(Integer.class,
                MethodConvert.i(Integer.class,
                        "Integer.parseInt($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)->s!=null ? Integer.parseInt(s) : null",
                        "TODO Number"
                ));

        MapConverters.put(int.class,
                MethodConvert.i(int.class,
                        "Integer.parseInt($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)->s!=null ? Integer.parseInt(s) : 0",
                        "TODO Number"
                ));

        MapConverters.put(Long.class,
                MethodConvert.i(Long.class,
                        "Long.parseLong($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)-> s!=null ?Long.parseLong(s) : null",
                        "TODO Number"
                ));

        MapConverters.put(long.class,
                MethodConvert.i(long.class,
                        "Long.parseLong($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)-> s!=null ?Long.parseLong(s) : 0",
                        "TODO Number"
                ));

        MapConverters.put(Double.class,
                MethodConvert.i(Double.class,
                        "Double.parseDouble($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)-> s!=null ? Double.parseDouble(s) : null",
                        "TODO Number"

                ));

        MapConverters.put(double.class,
                MethodConvert.i(double.class,
                        "Double.parseDouble($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)->s!=null ? Double.parseDouble(s): null",
                        "TODO Number"
                ));

        MapConverters.put(Float.class,
                MethodConvert.i(Float.class,
                        "Float.parseFloat($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)-> s!=null ?Float.parseFloat(s): null",
                        "TODO Number"
                ));

        MapConverters.put(float.class,
                MethodConvert.i(float.class,
                        "Float.parseFloat($$$.toString())",
                        "new JSONNumber($$$)",
                        "(s)-> s!=null ?Float.parseFloat(s): 0f",
                        "TODO NUmber"
                ));

        MapConverters.put(Boolean.class,
                MethodConvert.i(Boolean.class,
                        "Boolean.parseBoolean($$$.toString())",
                        "JSONBoolean.getInstance($$$)",
                        "(s)-> s!=null ? Boolean.parseBoolean(s): null",
                        "TODO Boolean"
                ));

        MapConverters.put(boolean.class,
                MethodConvert.i(boolean.class,
                        "Boolean.parseBoolean($$$.toString())",
                        "JSONBoolean.getInstance($$$)",
                        "(s)->s!=null ? Boolean.parseBoolean(s) : false",
                        "TODO Boolean"
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
