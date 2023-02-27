package co.antonis.generator.gson.model;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.gwt.CodeGenerator;
import co.antonis.generator.gson.gwt.CodeGeneratorUtilities;

import java.util.List;

/**
 * Wrapper of a "class" that needs to be serialized/deserialized,
 */
public class ClassInfo {

    //The Class that is serialized/deserialized
    String generatedClassName;
    Class<?> classToSerialize;
    List<FieldInfo> listFieldInfo;
    String methodFromJson;
    String methodToJson;

    public ClassInfo(Class<?> clazz, List<FieldInfo> listFieldInfo, String generatedClassName) {
        this(clazz,
                listFieldInfo,
                generatedClassName,
                Utilities.generateMethodName(clazz, true),
                Utilities.generateMethodName(clazz, false));
    }

    private ClassInfo(Class<?> clazz, List<FieldInfo> listFieldInfo, String generatedClassName, String methodFromJson, String methodToJson) {
        this.classToSerialize = clazz;
        this.listFieldInfo = listFieldInfo;
        this.generatedClassName = generatedClassName;
        this.methodFromJson = methodFromJson;
        this.methodToJson = methodToJson;
    }

    public Class<?> getClassToSerialize() {
        return classToSerialize;
    }

    public String getMethodFromJson() {
        return methodFromJson;
    }

    public String getGeneratedClassName() {
        return generatedClassName;
    }

    public String getMethodToJson() {
        return methodToJson;
    }

    public List<FieldInfo> getListFieldInfo() {
        return listFieldInfo;
    }

    /**
     * Return GeneratedClassName.fromXXXX(paramName)
     *
     * @param paramName
     * @return
     */
    public String code_methodFromJson(String paramName) {
        return code_methodFromJson(paramName, true);
    }

    /**
     * Return
     * 1. GeneratedClassName.fromXXXX(paramName)
     * 2  fromXXXX(paramName)
     *
     * @param paramName
     * @return
     */
    public String code_methodFromJson(String paramName, boolean isIncludeClassName) {
        return (isIncludeClassName ? (generatedClassName + ".") : "") + methodFromJson + "(" + paramName + ")";
    }

    public String code_methodFromJson_asFunction(String paramName, String as_methodReference) {
        if (as_methodReference == null)
            return "(" + paramName + ") -> " + code_methodFromJson(paramName);
        else
            return as_methodReference + "::" + methodFromJson;
    }

    public String code_methodToJson(String paramName) {
        return generatedClassName + "." + methodToJson + "(" + paramName + ")";
    }


}
