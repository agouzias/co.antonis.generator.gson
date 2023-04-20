package co.antonis.generator.gson;

import co.antonis.generator.gson.gwt.CodeGenerator;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Methods to be used/shared from other generators (apart gwt)
 */
public class Utilities {
    public static Logger log = Logger.getLogger(CodeGenerator.class.getName());

    //region Static Utilities
    public static String toUpperFirstLtr(String str) {
        if (str != null && str.length() > 0)
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        return null;
    }

    public static String generateMethodName(Class<?> clazz, boolean isFromJson) {
        if (isFromJson) {
            return "to" + clazz.getSimpleName().replaceAll("\\$", "_");
        } else {
            return "from" + clazz.getSimpleName().replaceAll("\\$", "_");
        }
    }
    //endregion
}
