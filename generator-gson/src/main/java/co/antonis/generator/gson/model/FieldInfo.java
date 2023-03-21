package co.antonis.generator.gson.model;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.gwt.CodeGenerator;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class FieldInfo {

    /*
     * The name that is used in 'json'
     */
    public String nameSerializable;

    /*
     * The actual name as defined in class
     */
    public String nameField;


    public Class<?> fieldClass;
    public Field field;

    public FieldInfo(Field field) {
        this.field = field;
        nameField = field.getName();
        fieldClass = field.getType();

        //Serializable name
        SerializedName anSerializableName = field.getAnnotation(SerializedName.class);
        nameSerializable = anSerializableName != null ? anSerializableName.value() : field.getName();
    }

    public boolean isPrimitive(){
        return fieldClass.isPrimitive();
    }

    public String nameUpper() {
        return Utilities.toUpperFirstLtr(nameField);
    }

    public boolean isSupportedPrimitive(Map<Class<?>, MethodConvert<?>> map) {
        return map.containsKey(fieldClass);
    }

    public boolean isSupportedContainer(List<Class<?>> list) {
        return list.contains(fieldClass);
    }

    public ClassInfo isPojo(List<ClassInfo> list) {
        for (ClassInfo cI : list) {
            if (fieldClass == cI.classToSerialize)
                return cI;
        }
        return null;
    }

    public String jsonObjGet() {
        return jsonObjGet("jsonObject", false);
    }

    public String jsonObjGet(String paramName, boolean isIncludeToString) {
        return paramName + ".get(\"" + nameSerializable + "\")" + (isIncludeToString ? ".toString()" : "");
    }

    public static String jsonObjPut(String key, String value){
        return "jsonObject.put(\""+key+"\","+value+")";
    }

    public String jsonObjPut(String value){
        return "jsonObject.put(\""+nameSerializable+"\","+value+")";
    }

    public String structureSet(String value) {
        if ((fieldClass == Boolean.class || fieldClass == boolean.class) && nameField.indexOf("is") == 0)
            return "structure.set" + Utilities.toUpperFirstLtr(nameField.substring(2)) + "(" + value + ")";
        else
            return "structure.set" + nameUpper() + "(" + value + ")";
    }



    public String structureGet() {
        // 'isBoolValue = "isBooleanValue()" , boolValue = 'isBoolValue()'
        if (fieldClass == Boolean.class || fieldClass == boolean.class)
            if (nameField.indexOf("is") == 0)
                return "structure.is" + Utilities.toUpperFirstLtr(nameField.substring(2)) + "()";
            else
                return "structure.is" + nameUpper() + "()";
        else
            return "structure.get" + nameUpper() + "()";
    }

    public String toSimpleString() {
        return "[" + nameField + "][" + fieldClass + "]";
    }

}
