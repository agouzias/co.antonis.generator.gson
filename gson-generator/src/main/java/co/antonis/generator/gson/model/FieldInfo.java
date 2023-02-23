package co.antonis.generator.gson.model;

import co.antonis.generator.gson.gwt.CodeGenerator;
import co.antonis.generator.gson.gwt.CodeGeneratorUtilities;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.List;

public class FieldInfo {
    public String nameSerializable;
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

    public String nameUpper() {
        return CodeGeneratorUtilities.toUpperFirstLtr(nameField);
    }


    public boolean isSupportedPrimitive() {
        return CodeGenerator.mapConvert_StringToValue.containsKey(fieldClass);
    }

    public boolean isSupportedContainer() {
        return CodeGenerator.ListClass_SupportedContainers.contains(fieldClass);
    }

    public ClassInfo isInPojo(List<ClassInfo> list){
        for(ClassInfo cI:list){
            if(fieldClass==cI.classToSerialize)
                return cI;
        }
        return null;
    }

    public String jsonObjGet() {
        return jsonObjGet("jsonObject",false);
    }
    public String jsonObjGet(String paramName, boolean isIncludeToString) {
        return paramName+".get(\"" + nameSerializable + "\")" + (isIncludeToString ? ".toString()" : "");
    }

    public String structureSet(String value){
        return "structure.set"+nameUpper()+"("+value+")";
    }

}
