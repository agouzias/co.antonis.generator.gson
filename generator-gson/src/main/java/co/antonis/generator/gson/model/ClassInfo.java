package co.antonis.generator.gson.model;

import co.antonis.generator.gson.gwt.CodeGeneratorUtilities;

import java.util.List;

public class ClassInfo {
    //The Class that is serialized
    Class<?> classToSerialize;
    List<FieldInfo> listFieldInfo;
    String methodFromJson;
    String methodToJson;

    public ClassInfo(Class<?> clazz, List<FieldInfo> listFieldInfo) {
        this(clazz, listFieldInfo, CodeGeneratorUtilities.generateMethodName(clazz, true), CodeGeneratorUtilities.generateMethodName(clazz, false));
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

    public String fromJson(String paramName) {
        return methodFromJson + "(" + paramName + ")";
    }

    public String getMethodToJson() {
        return methodToJson;
    }


    public List<FieldInfo> getListFieldInfo() {
        return listFieldInfo;
    }
}
