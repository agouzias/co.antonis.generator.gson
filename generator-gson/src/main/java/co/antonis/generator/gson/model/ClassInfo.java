package co.antonis.generator.gson.model;

import co.antonis.generator.gson.Utilities;

import java.util.List;

public class ClassInfo {
    //The Class that is serialized
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

    public String methodFromJson(String paramName) {
        return methodFromJson + "(" + paramName + ")";
    }

    public String methodToJson(String paramName) {
        return methodToJson + "(" + paramName + ")";
    }

    public String getMethodToJson() {
        return methodToJson;
    }


    public List<FieldInfo> getListFieldInfo() {
        return listFieldInfo;
    }
}
