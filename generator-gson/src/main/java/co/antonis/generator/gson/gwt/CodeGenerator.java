package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.model.MethodConvert;
import co.antonis.generator.gson.ListClasses;
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
import java.util.logging.Logger;
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
    public static Logger log = Logger.getLogger(CodeGenerator.class.getName());

    //region Members of code generation/POET
    static ClassName classJsonValue = ClassName.get("com.google.gwt.json.client", "JSONValue");
    static ClassName classJsonObject = ClassName.get("com.google.gwt.json.client", "JSONObject");
    static ClassName classJsonParser = ClassName.get("com.google.gwt.json.client", "JSONParser");
    static ClassName classDateFormat = ClassName.get("com.google.gwt.i18n.client", "DateTimeFormat");
    static ClassName classUtilities = ClassName.get("", "SerGwtUtils");
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
        List<Field> fields = new ArrayList<>(ListClasses.findFields(clazz, isOnlyWithExposeAnnotation ? Expose.class : null));
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

    /**
     * Generate implementation of Function<T,String> class
     * Generate implementation of Function<JSONValue, T> class
     * <p>
     * To be used in generation of container inputs
     * <p>
     * Example Function<T,String>
     * Function<Integer,String>         (s) -> s != null ? Integer.parseInt(s) : null
     * Function<Pojo,String>            (s) -> SerializationGWTJson_Sample.toPojoSimple(s))
     * Function<List<Pojo>,String>      (s0) -> SerGwtUtils.toListPojo_String_FuncS(
     * s0,
     * (s) -> SerializationGWTJson_Sample.toPojoSimple(s)
     * <p>
     * Function<T, JSONValue>
     * Function<Integer, JSONValue>     (num) -> new JSONNumber(num)
     * Function<String, JSONValue>     (pojo) -> new JSONString(pojo)
     *
     * @param fieldType, type of converter
     * @return code/string of the needed converter
     */
    public String generateCode_Lamda_Converter_StringToType_Or_TypeToJsonV(boolean isFromStringToType, Type fieldType, int iterationIndex) {

        if (fieldType instanceof Class) {

            /*
             * A. Primitive / Pojo / Enum
             */
            Class<?> fileTypeClass = (Class<?>) fieldType;
            if (MapConverter.MapConverters.containsKey(fileTypeClass)) {

                /*
                 * 1. "Primitive",  Function<T,String> / Function<JSONValue, T>
                 *  String:   (s) -> s!=null ? Long.parse(s) : null
                 *  Pojo:     (pojo) -> pojo!=null ? new JSONNumber(pojo) : null
                 */
                return isFromStringToType ?
                        converterOf(fileTypeClass).func_String_ToClass :
                        converterOf(fileTypeClass).func_Class_ToJsonV;

            } else if (fileTypeClass.isEnum()) {

                /*
                 * 2. "Enum"
                 * string:  (s) -> Enum.valueOf(s)
                 * pojo:    (pojo) -> pojo != null ? new JSONString(pojo.name()) : null
                 */
                return isFromStringToType ?
                        CodeBlock.builder().add("(s) -> s!=null ? " + code_string_to_enum("s") + " : null", fileTypeClass).build().toString() :
                        CodeBlock.builder().add("(pojo) -> pojo!=null ? new $T(" + code_enum_to_string("pojo") + ") : null", classJsonString).build().toString();

            } else {
                ClassInfo cI = findClassInfoWithType(fileTypeClass, listClassInfo);

                /*
                 * 3. "Object",
                 * string:  (s) -> pojoFromJson(s);
                 * pojo:    (pojo) -> pojoToJson(pojo)
                 */

                if (cI != null) {
                    if (isFromStringToType)
                        return cI.code_methodFromJson_asLamda("s", null);
                    else
                        return cI.code_methodToJson_asLamda("pojo", null);
                }

            }
        } else if (fieldType instanceof ParameterizedType) {

            /*
             *  4. The type is "Parameterized" meaning that we have probably a container
             *  and need set based on
             */

            if (isFromStringToType) {
                String parameter = "s" + iterationIndex; /*Creating a parameter that is not re-used*/
                PairStructure<String, Boolean> pair = CodeGenJsonFrom.generateCode_FromJson_SetField_Container(this, fieldType, parameter, "", false, iterationIndex + 1);
                return "(" + parameter + ")->" + pair.getFirst();
            } else {
                String parameter = "pojo" + iterationIndex; /*Creating a parameter that is not re-used*/
                PairStructure<String, Boolean> pair = CodeGenJsonTo.generateCode_ToJson_SetField_Container(this, fieldType, parameter, "", false, iterationIndex + 1);
                return "(" + parameter + ")->" + pair.getFirst();
                //return "todo";
            }
        }

        return null;
    }


    //endregion

    //region Methods Utilities
    static String code_toListPojo_fromV_methodS(boolean isParamJsonValue, String param, String convertMethod_from_s) {
        return prefix_In_Container + "$T.toListPojo_" + (isParamJsonValue ? "JsonV" : "String") + "_FuncS(\n" + param + ",\n" + convertMethod_from_s + ")\n";
    }

    static String code_toListJson(String param, String convertMethod_from_v) {
        return prefix_In_Container + "$T.toListJson_FuncJ(\n" + param + ",\n" + convertMethod_from_v + ")\n";
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

    static String code_toMapJson(String param, String convertMethod_key_from_v, String convertMethod_value_from_v) {
        return prefix_In_Container + "$T.toMapJson_FuncJ(\n" + param + ",\n" + convertMethod_key_from_v + ",\n" + convertMethod_value_from_v + ")\n";
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

    public static String toComment_safe(String comment) {
        return comment.replaceAll("\\$", "_");
    }

    public static PairStructure<String, Boolean> code(String code) {
        //Use 'add' instead of 'addStatement' to avoid ';'
        return PairStructure.n(CodeBlock.builder().add(code, classUtilities).build().toString(), true);
    }

    static String quote(String val) {
        return "\"" + val + "\"";
    }

    /**
     * In order to import the JSON classes adding a static block
     */
    public static CodeBlock addStaticBlockToImport() {
        return CodeBlock.builder()
                .addStatement("$T ignore1", classJsonString)
                .addStatement("$T ignore2", classJsonBoolean)
                .addStatement("$T ignore3", classJsonNumber)
                .build();
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

    private List<TypeSpec> generate_JavaCode(Set<Class<?>> setClasses) throws IOException {

        List<TypeSpec> listTypeBuilder = new ArrayList<>();
        mapClassGroupInfo.clear();
        listClassInfo.clear();

        /*
         * A. First Pass to gather ALL the generated classes AND the class-Names that will be used to generate the methods
         */
        setClasses.forEach((classToSerialize) -> {
            List<FieldInfo> listFieldInfo = listFields_OfClass(classToSerialize, this.isExportOnlyExpose)
                    .stream()
                    .map(fi->new FieldInfo(fi,classToSerialize)).collect(Collectors.toList());
            log.info("Preparing ["+classToSerialize.getName()+"] is to be generated/has fields ["+(listFieldInfo.size() > 0)+"]");

            if (listFieldInfo.size() > 0) {
                String className = CodeGenUtils.generateClassNameOf(classToSerialize, this);
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
         * C. For each class Generated the "from/to Json"
         */
        mapClassGroupInfo.values().forEach((classGroupI) -> {
            log.info("Generating Class ["+classGroupI.className+"]");
            classGroupI.listClassInfo.forEach((classInfo) -> {
                if (classInfo.getListFieldInfo().size() > 0) {
                    log.info("Generating ["+classInfo.getClassToSerialize()+"]");
                    if (isGenerateFromJsonMethods) {
                        MethodSpec.Builder method_convertFromJson = MethodSpec.methodBuilder(classInfo.getMethodFromJson()).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(String.class, "json").returns(classInfo.getClassToSerialize());

                        CodeGenJsonFrom.generateCode_FromJson_Method(this, method_convertFromJson, classInfo);

                        classGroupI.listMethodsFromJson.add(method_convertFromJson.build());
                    }

                    if (isGenerateToJsonMethods) {
                        MethodSpec.Builder method_convertToJson = MethodSpec.methodBuilder(classInfo.getMethodToJson()).addParameter(classInfo.getClassToSerialize(), "structure").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(classJsonObject);

                        CodeGenJsonTo.generateCode_ToJson_Method(this, method_convertToJson, classInfo);

                        classGroupI.listMethodsToJson.add(method_convertToJson.build());
                    }
                }
            });
        });


        mapClassGroupInfo.values().forEach((classGroupI) -> {
            log.info("Publishing Class ["+classGroupI.className+"] classes:"+classGroupI.listClassInfo.size());

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(classGroupI.className).addModifiers(Modifier.PUBLIC);
            if(isGenerateToJsonMethods)
                classBuilder.addStaticBlock(addStaticBlockToImport());

            //Java Doc for each class
            classBuilder.addJavadoc("Generated for total " + classGroupI.listClassInfo.size() + " structures \r\n\r\n");
            for (ClassInfo cI : classGroupI.listClassInfo) {
                classBuilder.addJavadoc(CodeGenerator.toComment_safe("@see " + cI.getClassToSerialize().getName() + " [fields:" + cI.getListFieldInfo().size() + "/" + cI.getClassToSerialize().getDeclaredFields().length + "] \r\n"));
            }

            //Logger and helper methods (if any)
            CodeGenUtils.addHelperMethods(this, classBuilder);

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
        listSpecs.add(CodeGenUtils.generate_javaCodeTypes(this, this.generatedClassName + "Types", isGenerateFromJsonMethods, isGenerateToJsonMethods));

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
