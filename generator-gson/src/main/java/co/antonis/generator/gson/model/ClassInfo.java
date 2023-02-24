package co.antonis.generator.gson.model;

import co.antonis.generator.gson.Utilities;

import java.util.List;

/**
 * Wrapper of a "class" that needs to be serialized/deserialized,
 */
public class ClassInfo {

    //The Class that is serialized/deserialized
    Class<?> classToSerialize;
    List<FieldInfo> listFieldInfo;
    String methodFromJson;
    String methodToJson;

    public ClassInfo(Class<?> clazz, List<FieldInfo> listFieldInfo) {
        this(clazz, listFieldInfo, Utilities.generateMethodName(clazz, true), Utilities.generateMethodName(clazz, false));
    }

    public ClassInfo(Class<?> clazz, List<FieldInfo> listFieldInfo, String methodFromJson, String methodToJson) {
        this.classToSerialize = clazz;
        this.listFieldInfo = listFieldInfo;
        this.methodFromJson = methodFromJson;
        this.methodToJson = methodToJson;
    }

    public Class<?> getClassToSerialize() {
        return classToSerialize;
    }

    public String getMethodFromJson() {
        return methodFromJson;
    }


    public String getMethodToJson() {
        return methodToJson;
    }

    public List<FieldInfo> getListFieldInfo() {
        return listFieldInfo;
    }

    public String code_methodFromJson(String paramName) {
        return methodFromJson + "(" + paramName + ")";
    }

    public String code_methodFromJson_asFunction(String paramName, String as_methodReference) {
        if (as_methodReference == null)
            return "(" + paramName + ") -> " + code_methodFromJson(paramName);
        else
            return as_methodReference + "::" + methodFromJson;
    }

    public String code_methodToJson(String paramName) {
        return methodToJson + "(" + paramName + ")";
    }

}
