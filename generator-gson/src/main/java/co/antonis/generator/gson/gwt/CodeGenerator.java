package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.model.MethodConvert;
import co.antonis.generator.gson.ReaderJava;
import co.antonis.generator.gson.model.ClassInfo;
import co.antonis.generator.gson.model.FieldInfo;
import co.antonis.generator.gson.model.PairStructure;
import com.google.gson.annotations.Expose;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.io.File;
import java.util.stream.Collectors;

/**
 * Get a Set<Class> and export a Java serialization (convert methods) code from/to Json
 * To be used in GWT (GWTParser).
 * <p>
 * <p>
 * Sample Code
 * JSONValue jsonValue = JSONParser.parseStrict(json);
 * JSONObject jsonObject = jsonValue.isObject();
 * InfoMessageStructure result = null;
 * if (jsonObject != null) {
 * result = new InfoMessageStructure();
 * result.setMessage(toString(jsonObject.get("msg")));
 * result.setType(toInt(jsonObject.get("t"), 0));
 * result.setMessageExtended(toString(jsonObject.get("mse")));
 * }
 * return result;
 */
@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class CodeGenerator {

    //region Members of code generation/POET
    static ClassName classJsonValue = ClassName.get("com.google.gwt.json.client", "JSONValue");
    static ClassName classJsonObject = ClassName.get("com.google.gwt.json.client", "JSONObject");
    static ClassName classJsonParser = ClassName.get("com.google.gwt.json.client", "JSONParser");
    static ClassName classDateFormat = ClassName.get("com.google.gwt.i18n.client", "DateTimeFormat");
    static ClassName classUtilities = ClassName.get("", "SerializationGWTUtilities");
    static ClassName classJsonString = ClassName.get("com.google.gwt.json.client", "JSONString");
    static ClassName classJsonNumber = ClassName.get("com.google.gwt.json.client", "JSONNumber");
    static ClassName classJsonBoolean = ClassName.get("com.google.gwt.json.client", "JSONBoolean");

    static String prefix_In_Container = "" /*"\n"*/;
    static String NOT_IMPLEMENTED = "Not implemented. Consider to add it manually or ignore field";
    //endregion

    //region Members Configuration
    /**
     * The classic scenario is to construct a single class in
     * generatedPackageName.generatedClassNameSingle with all the methods.
     * <p>
     * Alternative you can construct a classes per package
     * generatedPackageName.generateClassName with replace "$"
     */
    public String generatedPackageName = "co.antonis.gwt.generated";
    public String generatedClassName = "SerializerGWTJson";
    public boolean isGenerateSeparateClassPerPackage = false;
    public File filePathToGenerated = new File(".");
    public boolean isExportOnlyExpose = true;
    public boolean isGenerateFromJsonMethods = true;
    public boolean isGenerateToJsonMethods = true;

    public boolean isCheckNotNullValue_BeforeUse = true;
    public boolean isPrintLogInfo = false;
    //endregion

    //region Members generated
    List<ClassInfo> listClassInfo = new ArrayList<>();
    Map<String, ClassGroupInfo> mapClassGroupInfo = new HashMap<>();

    //endregion

    //region Static Converters

    public static List<Class<?>> listSupportedContainers = Arrays.asList(List.class, Map.class);
    //endregion

    //region Methods Setters
    public CodeGenerator() {
    }

    /**
     * Print in generated methods log.info(..) for debuging
     * By default is false
     *
     * @param printLogInfo param
     * @return the CodeGenerator
     */
    public CodeGenerator setPrintLogInfo(boolean printLogInfo) {
        isPrintLogInfo = printLogInfo;
        return this;
    }

    /**
     * If true generated 'XXXToJson(Object) Methods,
     * By default is true
     *
     * @param generateToJson param
     * @return the CodeGenerator
     */
    public CodeGenerator setGenerateToJsonMethods(boolean generateToJson) {
        isGenerateToJsonMethods = generateToJson;
        return this;
    }

    /**
     * If true generates 'XXXFromJson(String s)' methods
     * By default is true
     *
     * @param generateFromJson param
     * @return the CodeGenerator
     */
    public CodeGenerator setGenerateFromJsonMethods(boolean generateFromJson) {
        isGenerateFromJsonMethods = generateFromJson;
        return this;
    }

    /**
     * The package name of the generated classes
     *
     * @param generatedPackageName param
     * @return the CodeGenerator
     */
    public CodeGenerator setGeneratedPackageName(String generatedPackageName) {
        this.generatedPackageName = generatedPackageName;
        return this;
    }

    /**
     * The name of the generated class, by default is 'SerializerGWTJson'
     * <p>
     * If the second parameter 'isSeparateClassPerPackage' is true generated instead of one class with all the methods,
     * multiple classes based on package name of each pojo. (replace the $)
     *
     * @param generatedClassName        param
     * @param isSeparateClassPerPackage param
     * @return the CodeGenerator
     */
    public CodeGenerator setGeneratedClassName(String generatedClassName, boolean isSeparateClassPerPackage) {
        this.generatedClassName = generatedClassName;
        this.isGenerateSeparateClassPerPackage = isSeparateClassPerPackage;
        return this;
    }

    /**
     * Serialize ONLY the fields with the 'Expose' annotation
     * by default is true
     *
     * @param exportOnlyExpose, param
     * @return the CodeGenerator
     */
    public CodeGenerator setExportOnlyExpose(boolean exportOnlyExpose) {
        isExportOnlyExpose = exportOnlyExpose;
        return this;
    }

    public CodeGenerator setFilePathOfGeneratedFiles(File filePathToGenerated) {
        this.filePathToGenerated = filePathToGenerated;
        return this;
    }
    //endregion

    //region Finders, Filters & Lists
    static List<Field> listFields_OfClass(Class<?> clazz, boolean isOnlyWithExposeAnnotation) {
        // Get fields
        List<Field> fields = new ArrayList<>(ReaderJava.findFields(clazz, isOnlyWithExposeAnnotation ? Expose.class : null));
        List<Field> fieldsFiltered = new ArrayList<>();

        // Search
        for (Field field : fields) {
            Annotation annot = field.getAnnotation(Expose.class);

            //Check if field is Primitive (well .. just ignore)
            //boolean isSupportedPrimitive = mapConvert_StringToValue.containsKey(field.getClass());

            if (isOnlyWithExposeAnnotation) {
                if (annot != null) fieldsFiltered.add(field);
            } else {
                //Adding even if it is not supported (in order to add comment)
                fieldsFiltered.add(field);
            }
        }
        return fieldsFiltered;
    }

    static ClassInfo findClassInfoWithType(Class<?> clazz, List<ClassInfo> listClassInfo) {
        List<ClassInfo> cI_list = listClassInfo.stream().filter((cI) -> cI.getClassToSerialize() == clazz).collect(Collectors.toList());

        if (cI_list.size() > 0) {
            //TODO should i warn? if greater than one
            return cI_list.get(0);
        }
        return null;
    }

    /**
     * Finder of Converter.
     *
     * @param clazz, input class
     * @return methodConverter of class
     */
    public static MethodConvert<?> converterOf(Class<?> clazz) {
        return MapConverter.MapConverters.get(clazz);
    }

    //endregion

    //region Methods To Json

    /**
     * Generate method "toJson(Pojo)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param method, the method spec
     * @param cI,     the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public MethodSpec.Builder generateCode_ToJson_Method(MethodSpec.Builder method, ClassInfo cI) {
        Utilities.log.info("Class To JSON [" + cI.getClassToSerialize() + "]");

        List<FieldInfo> listFields = cI.getListFieldInfo();
        method.beginControlFlow("if (structure == null) ").addStatement("return null").endControlFlow();

        //Generate JSONObject (jsonObject)
        method.addStatement("$T jsonObject = new $T()", classJsonObject, classJsonObject);
        //For ALL exposed fields
        listFields.forEach(fieldI -> {
            generateCode_ToJson_SetField(method, fieldI);
        });
        method.addStatement("return jsonObject");
        return method;
    }

    @SuppressWarnings("Duplicates")
    public void generateCode_ToJson_SetField(MethodSpec.Builder method, FieldInfo fI) {
        Utilities.log.info("Preparing Field [" + fI.nameField + "] type [" + fI.fieldClass + "]");

        Class<?> classOfField = fI.fieldClass;
        ClassInfo cInfo_OfField = findClassInfoWithType(classOfField, listClassInfo);
        boolean isSupported = MapConverter.MapConverters.containsKey(classOfField) || cInfo_OfField != null || classOfField.isEnum() || listSupportedContainers.contains(classOfField);

        // Always print debug is supported
        if (isPrintLogInfo) {
            method.addStatement(code_toLog(quote("Field:" + fI.nameField + ":") + "+" + fI.structureGet(), true));
        }

        if (!isSupported) {

            method.addComment(CodeGeneratorUtilities.toComment_safe("TODO " + fI.toSimpleString() + " Warning, Not supported type, the field will be ignored."));

        } else {

            // Check if not null
            if (isCheckNotNullValue_BeforeUse && !fI.isPrimitive()) method.beginControlFlow("if(" + fI.structureGet() + "!=null)");

            if (MapConverter.MapConverters.containsKey(classOfField)) {

                /*
                 * A. Case Primitive Type
                 *  jsonObject.put("parameterName",new JSONString(structure.getParameterName()));
                 *  jsonObject.put("intSimple",new JSONNumber(structure.getIntSimple()));
                 */

                String convertCode = converterOf(classOfField).inline_Class_ToJsonV(fI.structureGet());
                if (convertCode == null) method.addComment(NOT_IMPLEMENTED);
                else method.addStatement(FieldInfo.jsonObjPut(fI.nameSerializable, convertCode));

            } else if (cInfo_OfField != null) {

                /*
                 * B. Pojo, the field is another pojo, use a 'fromJson(String)' to convert it.
                 * jsonObject.put("child",SerializationGWTJson_Sample.fromPojoSimple(structure.getSimple()));
                 */

                String convertCode = cInfo_OfField.code_methodToJson(fI.structureGet());
                method.addStatement(FieldInfo.jsonObjPut(fI.nameSerializable, convertCode));

            } else if (fI.fieldClass.isEnum()) {

                /*
                 * C. Case Enum
                 * jsonObject.put("nameOfType",new JSONString(structure.getPojoType().name()));
                 */

                String convertCode = converterOf(String.class).inline_Class_ToJsonV(code_enum_to_string(fI.structureGet()));
                method.addStatement(FieldInfo.jsonObjPut(fI.nameSerializable, convertCode));

            } else if (fI.isSupportedContainer(listSupportedContainers)) {

                /*
                 * D. Case Container of Primitive or Supported Types
                 */

                /* Using converter containers.
                   jsonObject.put("test",
                            SerializationGWTUtilities.toListJson_FuncJ(
                                structure.getListString(),
                                (s)->new JSONString(s))
                   )
                 */
                generateCode_ToJson_SetField_Container(
                        fI.field.getGenericType(),
                        "todo",
                        "[Member:]..",
                        true,0
                );

            }

            if (isCheckNotNullValue_BeforeUse && !fI.isPrimitive()) method.endControlFlow();

        }
    }

    /**
     *
     *
     * SerializationGWTUtilities.toListJson_FuncJ(
     *      structure.getListString(),
     *      (s)->new JSONString(s)
     * )
     *
     *
     * @param type,                     the field type
     * @param paramName,                the parameter name to be used
     * @param fieldName_ForComments,    used for comments
     * @param isParamJsonValue,         todo
     * @param iterationIndex,           todo
     * @return generated code that converts pojo to json value
     */
    @SuppressWarnings("Duplicates")
    public PairStructure<String, Boolean> generateCode_ToJson_SetField_Container(Type type, String paramName, String fieldName_ForComments, boolean isParamJsonValue, int iterationIndex) {

        if (type instanceof ParameterizedType) {

            ParameterizedType typeGeneric = (ParameterizedType) type;
            Class<?> fieldClass = (Class<?>) typeGeneric.getRawType();
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fieldClass == List.class) {

                String methodConvert_item = generateCode_FunctionFromStringToClass(arguments[0], iterationIndex);
                return code(code_toListJson_methodS(/*isParamJsonValue,*/ paramName, methodConvert_item));

            } else if (fieldClass == Map.class) {

                String methodConvert_key = generateCode_FunctionFromStringToClass(arguments[0], iterationIndex);
                String methodConvert_value = generateCode_FunctionFromStringToClass(arguments[1], iterationIndex);

                return code(code_toMapPojo_fromV_methodS(isParamJsonValue, paramName, methodConvert_key, methodConvert_value));

            } else {
                return comment("TODO " + fieldName_ForComments + " Not implemented container");
            }

        } else {
            return comment("TODO " + fieldName_ForComments + " Not type parameters in container (ignoring)");
        }

    }

    //endregion

    //region Method From Json

    /**
     * Generate method "fromJson(Class, String)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param method, the method spec
     * @param cI,     the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public MethodSpec.Builder generateCode_FromJson_Method(MethodSpec.Builder method, ClassInfo cI) {
        Utilities.log.info("Class From JSON [" + cI.getClassToSerialize() + "]");

        List<FieldInfo> listFields = cI.getListFieldInfo();

        method.addStatement("$T jsonValue = $T.parseStrict(json)", classJsonValue, classJsonParser).addStatement("$T jsonObject = jsonValue.isObject()", classJsonObject).beginControlFlow("\r\nif (jsonObject == null) ").addStatement("return null").endControlFlow();

        //Generate POJO (structure)
        method.addStatement("$T structure = new $T()", cI.getClassToSerialize(), cI.getClassToSerialize());
        //For ALL exposed fields
        listFields.forEach((fieldI -> {
            generateCode_FromJson_SetField(method, fieldI);
        }));
        method.addStatement("return structure");

        return method;
    }

    /**
     * Generate code to set field of a pojo.
     * Check the type of the field (primitive, container, pojo, enum)
     * and generate the correct setXXX(....)
     *
     * @param fI, the fieldInfo to set
     */
    @SuppressWarnings("Duplicates")
    public void generateCode_FromJson_SetField(MethodSpec.Builder method, FieldInfo fI) {
        Utilities.log.info("Preparing Field [" + fI.nameField + "] type [" + fI.fieldClass + "]");

        Class<?> classOfField = fI.fieldClass;
        ClassInfo cInfo_OfField = findClassInfoWithType(classOfField, listClassInfo);
        boolean isSupported = MapConverter.MapConverters.containsKey(classOfField) || cInfo_OfField != null || classOfField.isEnum() || listSupportedContainers.contains(classOfField);

        // Always print debug is supported
        if (isPrintLogInfo) {
            method.addStatement(code_toLog(quote("Field:" + fI.nameField + ":") + "+" + fI.jsonObjGet(), true));
        }

        // Return if it is not supported
        if (!isSupported) {

            method.addComment(CodeGeneratorUtilities.toComment_safe("TODO " + fI.toSimpleString() + " Warning, Not supported type, the field will be ignored."));

        } else {

            // Check if not null
            if (isCheckNotNullValue_BeforeUse) method.beginControlFlow("if(" + fI.jsonObjGet() + "!=null)");

            if (MapConverter.MapConverters.containsKey(classOfField)) {

                /*
                 * A. Case Primitive Type
                 *  structure.setParameterName(Long.parse(parameterName))
                 */

                //Using Inline function of JSON to Primitive
                String convertCode = converterOf(fI.fieldClass).inline_JsonV_ToClass(fI.jsonObjGet());
                if (convertCode == null) {
                    method.addComment(NOT_IMPLEMENTED);
                } else {
                    method.addStatement(fI.structureSet(convertCode), classUtilities);
                }


            } else if (cInfo_OfField != null) {

                /*
                 * B. Pojo, the field is another pojo, use a 'fromJson(String)' to convert it.
                 * code = toXXXFromJson(parameterName)
                 */

                String convertCode = cInfo_OfField.code_methodFromJson(MapConverter.MethodConvert_ofPojo.inline_JsonV_ToClass(fI.jsonObjGet()));
                method.addStatement(fI.structureSet(convertCode), classUtilities);

            } else if (fI.fieldClass.isEnum()) {

                /*
                 * C. Case Enum
                 * Enum.valueOf( parameterName.isString().stringValue() )
                 */

                String convertCode = converterOf(String.class).inline_JsonV_ToClass(fI.jsonObjGet());
                method.addStatement(fI.structureSet(code_string_to_enum(convertCode)), fI.fieldClass);

            } else if (fI.isSupportedContainer(listSupportedContainers)) {

                /*
                 * D. Case Container of Primitive or Supported Types
                 */

                /* Using converter containers.
                setXXX(
                   SerializationGWTUtilities.toListPojo_JsonV_FuncS(
                            jsonObject.get("listDateSimple"),
                           (s)->SerializationGWTUtilities.toDateFromS(s)))
                  )
                 */
                PairStructure<String, Boolean> codePair = generatedCode_FromJson_SetField_Container(
                        fI.field.getGenericType(),
                        fI.jsonObjGet(),
                        "[Member:" + fI.nameField + "/ Serializable:" + fI.nameSerializable + "] (" + fI.field.getGenericType() + ")",
                        true,
                        0);

                if (codePair.getSecond()) method.addStatement(fI.structureSet(codePair.getFirst()));
                else method.addComment(codePair.getFirst());
            }

            if (isCheckNotNullValue_BeforeUse) method.endControlFlow();
        }
    }

    //endregion

    //region Methods From Json (container related)

    /**
     * Generate Code that converts a JSONValue OR String to Parameterized Container with the input type.
     * The type-arguments must be checked for Primitives, Pojo's, Enum or Containers and use the needed converted methods
     *
     * SerializationGWTUtilities.toListPojo_JsonV_FuncS(
     *      jsonObject.get("listDateSimple"),
     *      (s)->SerializationGWTUtilities.toDateFromS(s)))
     * )
     *
     *
     * @param type,                  the field type
     * @param paramName,             the param name to be used  (convertMethod(paramName) )
     * @param fieldName_ForComments, the field info of the container
     * @param isParamJsonValue,      If true the return converted methods expect 'JSONValue' otherwise expect 'String'
     * @param iterationIndex,        the field info of the container
     * @return generated code that converts json to container
     */
    @SuppressWarnings("Duplicates")
    public PairStructure<String, Boolean> generatedCode_FromJson_SetField_Container(Type type, String paramName, String fieldName_ForComments, boolean isParamJsonValue, int iterationIndex) {

        if (type instanceof ParameterizedType) {

            ParameterizedType typeGeneric = (ParameterizedType) type;
            Class<?> fieldClass = (Class<?>) typeGeneric.getRawType();
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fieldClass == List.class) {

                String methodConvert_item = generateCode_FunctionFromStringToClass(arguments[0], iterationIndex);
                return code(code_toListPojo_fromV_methodS(isParamJsonValue, paramName, methodConvert_item));

            } else if (fieldClass == Map.class) {

                String methodConvert_key = generateCode_FunctionFromStringToClass(arguments[0], iterationIndex);
                String methodConvert_value = generateCode_FunctionFromStringToClass(arguments[1], iterationIndex);

                return code(code_toMapPojo_fromV_methodS(isParamJsonValue, paramName, methodConvert_key, methodConvert_value));

            } else {
                return comment("TODO " + fieldName_ForComments + " Not implemented container");
            }

        } else {
            return comment("TODO " + fieldName_ForComments + " Not type parameters in container (ignoring)");
        }

    }

    /**
     * Generate implementation of Function<T,String> class
     * This is the generator of
     *
     * @param fieldType, type of converter
     * @return code/string of the needed converter
     */
    public String generateCode_FunctionFromStringToClass(Type fieldType, int iterationIndex) {
        if (fieldType instanceof Class) {

            /*
             * A. Primitive / Pojo / Enum
             */
            Class<?> fileTypeClass = (Class<?>) fieldType;
            if (MapConverter.MapConverters.containsKey(fileTypeClass)) {

                /*
                 * 1. "Primitive", find the Function<String,K> lamda function to use
                 *  example: (s) -> s!=null ? Long.parse(s) : null
                 */
                return converterOf(fileTypeClass).func_String_ToClass;

            } else if (fileTypeClass.isEnum()) {

                /*
                 * 2. "Enum"
                 * example: (s) -> s!=null ? Enum.valueOf(s)
                 */
                return CodeBlock.builder().add("(s) -> " + code_string_to_enum("s"), fileTypeClass).build().toString();

            } else {
                ClassInfo cI = findClassInfoWithType(fileTypeClass, listClassInfo);

                /*
                 * 3. The type is "Object", find the Function<String,K> Lamda function to use
                 */
                if (cI != null) {
                    return cI.code_methodFromJson_asFunction("s", null); //"(s) -> " + cI.methodFromJson("s");
                }

            }
        } else if (fieldType instanceof ParameterizedType) {
            /*
             *  3. The type is "Parameterized" meaning that we have probably a container
             *  and need set based on
             */
            String parameter = "s" + iterationIndex; /*Creating a parameter that is not re-used*/
            PairStructure<String, Boolean> pair = generatedCode_FromJson_SetField_Container(fieldType, parameter, "", false, iterationIndex + 1);
            return "(" + parameter + ")->" + pair.getFirst();
            //return "null /*TODO Implement the data*/";
        }
        return null;
    }


    /**
     * Generate implementation of Function<T,String> class
     * Generate implementation of Function<JSONValue, T> class
     * This is the generator of
     *
     * @param fieldType, type of converter
     * @return code/string of the needed converter
     */
    public String generateCode_Func_StringToClass_Or_ClassToJsonV(Type fieldType, boolean isFromStringToClass, int iterationIndex) {

        if (fieldType instanceof Class) {

            /*
             * A. Primitive / Pojo / Enum
             */
            Class<?> fileTypeClass = (Class<?>) fieldType;
            if (MapConverter.MapConverters.containsKey(fileTypeClass)) {

                /*
                 * 1. "Primitive", find the Function<T,String> lamda function to use
                 *  example: (s) -> s!=null ? Long.parse(s) : null
                 */
                return isFromStringToClass ?
                        converterOf(fileTypeClass).func_String_ToClass:
                        converterOf(fileTypeClass).func_Class_ToJsonV;

            } else if (fileTypeClass.isEnum()) {
asfasdfasdfasdfasdfasdfa
                /*
                 * 2. "Enum"
                 * example: (s) -> s!=null ? Enum.valueOf(s)
                 */
                return isFromStringToClass ?
                        CodeBlock.builder().add("(s) -> " + code_string_to_enum("s"), fileTypeClass).build().toString();
                        CodeBlock.builder().add("(pojo) -> " + code_enum_to_string("s"), fileTypeClass).build().toString();

            } else {
                ClassInfo cI = findClassInfoWithType(fileTypeClass, listClassInfo);

                /*
                 * 3. The type is "Object", find the Function<String,K> Lamda function to use
                 */
                if (cI != null) {
                    return cI.code_methodFromJson_asFunction("s", null); //"(s) -> " + cI.methodFromJson("s");
                }

            }
        } else if (fieldType instanceof ParameterizedType) {
            /*
             *  3. The type is "Parameterized" meaning that we have probably a container
             *  and need set based on
             */
            String parameter = "s" + iterationIndex; /*Creating a parameter that is not re-used*/
            PairStructure<String, Boolean> pair = generatedCode_FromJson_SetField_Container(fieldType, parameter, "", false, iterationIndex + 1);
            return "(" + parameter + ")->" + pair.getFirst();
            //return "null /*TODO Implement the data*/";
        }
        return null;
    }


    //endregion

    //region Methods Utilities
    static String code_toListPojo_fromV_methodS(boolean isParamJsonValue, String param, String convertMethod_from_s) {
        return prefix_In_Container + "$T.toListPojo_" + (isParamJsonValue ? "JsonV" : "String") + "_FuncS(\n" + param + ",\n" + convertMethod_from_s + ")\n";
    }

    static String code_toListJson_methodS(/*boolean isParamJsonValue,*/ String param, String convertMethod_from_v) {
        return prefix_In_Container + "$T.toListJson_" + /*(isParamJsonValue ? "JsonV" : "String")*/ "FuncJ(\n" + param + ",\n" + convertMethod_from_v + ")\n";
    }

    /**
     * Utils.toMap_FromString OR toMap_FromString (param, function(), function())
     * Generate java-code that converts a parameter (string OR jsonV) to Map<K,V>
     * The Key, Value converters are always converting from String.
     *
     * @param isParamJsonValue,           if the parameter is String or JsonV
     * @param param,                      the parameter name
     * @param convertMethod_key_from_s,   the convert Key
     * @param convertMethod_value_from_s, the convert Value
     */
    static String code_toMapPojo_fromV_methodS(boolean isParamJsonValue, String param, String convertMethod_key_from_s, String convertMethod_value_from_s) {
        return prefix_In_Container + "$T.toMapPojo_" + (isParamJsonValue ? "JsonV" : "String") + "_FuncS(\n" + param + ",\n" + convertMethod_key_from_s + ",\n" + convertMethod_value_from_s + ")\n";
    }

    static String code_string_to_enum(String param) {
        return "$T.valueOf(" + param + ")";
    }

    static String code_enum_to_string(String param) {
        return param + ".name()";
    }

    static String code_toLog(String msg, boolean isOmitQuotes) {
        return "log.info(" + (isOmitQuotes ? "" : "\"") + msg + (isOmitQuotes ? ")" : "\")");
    }

    public static PairStructure<String, Boolean> comment(String code) {
        return PairStructure.n(code, false);
    }

    public static PairStructure<String, Boolean> code(String code) {
        //Use 'add' instead of 'addStatement' to avoid ';'
        return PairStructure.n(CodeBlock.builder().add(code, classUtilities).build().toString(), true);
    }

    static String quote(String val) {
        return "\"" + val + "\"";
    }
    //endregion

    static class ClassGroupInfo {
        public String className;
        //Class to be created
        public List<ClassInfo> listClassInfo = new ArrayList<>();
        public List<MethodSpec> listMethodsFromJson = new ArrayList<>();
        public List<MethodSpec> listMethodsToJson = new ArrayList<>();

        public ClassGroupInfo(String className) {
            this.className = className;
        }
    }

    public List<TypeSpec> generate_JavaCode(Set<Class<?>> setClasses) throws IOException {

        List<TypeSpec> listTypeBuilder = new ArrayList<>();
        mapClassGroupInfo.clear();
        listClassInfo.clear();

        /*
         * A. First Pass to gather ALL the generated classes AND the class-Names that will be used to generate the methods
         */
        setClasses.forEach((classToSerialize) -> {
            List<FieldInfo> listFieldInfo = listFields_OfClass(classToSerialize, this.isExportOnlyExpose).stream().map(FieldInfo::new).collect(Collectors.toList());
            if (listFieldInfo.size() > 0) {
                String className = CodeGeneratorUtilities.generateClassNameOf(classToSerialize, this);
                ClassInfo cI = new ClassInfo(classToSerialize, listFieldInfo, className);
                if (!mapClassGroupInfo.containsKey(className)) {
                    mapClassGroupInfo.put(className, new ClassGroupInfo(className));
                }

                //Add to containers
                listClassInfo.add(cI);
                mapClassGroupInfo.get(className).listClassInfo.add(cI);
            }
        });

        /*
         * C. For each class Generated the "from Json"
         */
        mapClassGroupInfo.values().forEach((classGroupI) -> {
            classGroupI.listClassInfo.forEach((classInfo) -> {
                if (classInfo.getListFieldInfo().size() > 0) {
                    if (isGenerateFromJsonMethods) {
                        MethodSpec.Builder method_convertFromJson = MethodSpec.methodBuilder(classInfo.getMethodFromJson()).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(String.class, "json").returns(classInfo.getClassToSerialize());

                        generateCode_FromJson_Method(method_convertFromJson, classInfo);

                        classGroupI.listMethodsFromJson.add(method_convertFromJson.build());
                    }

                    if (isGenerateToJsonMethods) {
                        MethodSpec.Builder method_convertToJson = MethodSpec.methodBuilder(classInfo.getMethodToJson()).addParameter(classInfo.getClassToSerialize(), "structure").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(classJsonObject);

                        generateCode_ToJson_Method(method_convertToJson, classInfo);

                        classGroupI.listMethodsToJson.add(method_convertToJson.build());
                    }
                }
            });
        });


        mapClassGroupInfo.values().forEach((classGroupI) -> {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(classGroupI.className).addModifiers(Modifier.PUBLIC);

            //Java Doc for each class
            classBuilder.addJavadoc("Generated for total " + classGroupI.listClassInfo.size() + " structures \r\n\r\n");
            for (ClassInfo cI : classGroupI.listClassInfo) {
                classBuilder.addJavadoc(CodeGeneratorUtilities.toComment_safe("@see " + cI.getClassToSerialize().getName() + " [fields:" + cI.getListFieldInfo().size() + "/" + cI.getClassToSerialize().getDeclaredFields().length + "] \r\n"));
            }

            //Logger and helper methods (if any)
            CodeGeneratorUtilities.addHelperMethods(this, classBuilder);

            /*
             * D. Add Methods "from/to Json"
             */
            for (int i = 0; i < classGroupI.listClassInfo.size(); i++) {
                if (isGenerateFromJsonMethods) {
                    classBuilder.addMethod(classGroupI.listMethodsFromJson.get(i));
                }
                if (isGenerateToJsonMethods) {
                    classBuilder.addMethod(classGroupI.listMethodsToJson.get(i));
                }
            }

            listTypeBuilder.add(classBuilder.build());
        });

        return listTypeBuilder;
    }

    public List<JavaFile> build(Set<Class<?>> setClasses) throws IOException {
        List<JavaFile> listJavaFile = new ArrayList<>();

        List<TypeSpec> listSpecs = generate_JavaCode(setClasses);
        listSpecs.add(CodeGeneratorUtilities.generate_javaCodeTypes(this, this.generatedClassName + "Types", isGenerateFromJsonMethods, isGenerateToJsonMethods));

        /*
         * E. Export
         */
        for (TypeSpec listSpec : listSpecs) {
            JavaFile javaFile = JavaFile.builder(generatedPackageName, listSpec).build();

            if (filePathToGenerated != null) {
                if (filePathToGenerated.isDirectory()) javaFile.writeTo(filePathToGenerated);
                else throw new RuntimeException("Ensure that the exported path is directory [" + filePathToGenerated.getAbsolutePath() + "]");
            }
        }

        return listJavaFile;
    }
}

