package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.model.MethodConvert;
import co.antonis.generator.gson.ReaderJava;
import co.antonis.generator.gson.model.ClassInfo;
import co.antonis.generator.gson.model.FieldInfo;
import co.antonis.generator.gson.model.PairStructure;
import com.google.common.reflect.TypeParameter;
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
public class CodeGenerator {

    //region Members of code generation/POET
    static ClassName classJsonValue = ClassName.get("com.google.gwt.json.client", "JSONValue");
    static ClassName classJsonObject = ClassName.get("com.google.gwt.json.client", "JSONObject");
    static ClassName classJsonParser = ClassName.get("com.google.gwt.json.client", "JSONParser");
    static ClassName classDateFormat = ClassName.get("com.google.gwt.i18n.client", "DateTimeFormat");
    static ClassName classUtilities = ClassName.get("", "SerializationGWTUtilities");
    //endregion

    //region Members Configuration
    String generatedPackageName = "co.antonis.gwt.generated";
    String generatedClassNameSingle = "SerializerGWTJson";
    String useClassNamePerPackage = null;
    boolean isExportOnlyExpose = true;
    File filePathToGenerated = new File(".");

    boolean isCheckNotNullTheJsonValue_BeforeSetJavaField = true;
    boolean isPrintLogInfo = false;
    boolean isUseUtilsForPrimitive = false;
    //endregion

    //region Members generated
    List<ClassInfo> listClass_Generated = new ArrayList<>();
    //endregion

    //region Static Converters
    public static Map<Class<?>, MethodConvert<?>> mapConvert_StringToValue = new HashMap<>();

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
        mapConvert_StringToValue.put(String.class, MethodConvert.i(String.class, "$$$.isString().stringValue()", "(s)->s")); /*not use s.toString() in function to avoid null pointer*/
        mapConvert_StringToValue.put(Date.class, MethodConvert.i(Date.class, "toDateFromV($$$/*.isString().stringValue()*/)", "(s)->toDateFromS(s)"));

        mapConvert_StringToValue.put(Integer.class, MethodConvert.i(Integer.class, "Integer.parseInt($$$.toString())", "(s)->s!=null ? Integer.parseInt(s) : null"));
        mapConvert_StringToValue.put(int.class, MethodConvert.i(int.class, "Integer.parseInt($$$.toString())", "(s)->s!=null ? Integer.parseInt(s) : 0"));
        mapConvert_StringToValue.put(Long.class, MethodConvert.i(Long.class, "Long.parseLong($$$.toString())", "(s)-> s!=null ?Long.parseLong(s) : null"));
        mapConvert_StringToValue.put(long.class, MethodConvert.i(long.class, "Long.parseLong($$$.toString())", "(s)-> s!=null ?Long.parseLong(s) : 0"));
        mapConvert_StringToValue.put(Double.class, MethodConvert.i(Double.class, "Double.parseDouble($$$.toString())", "(s)-> s!=null ? Double.parseDouble(s) : null"));
        mapConvert_StringToValue.put(double.class, MethodConvert.i(double.class, "Double.parseDouble($$$.toString())", "(s)->s!=null ? Double.parseDouble(s): null"));
        mapConvert_StringToValue.put(Float.class, MethodConvert.i(Float.class, "Float.parseFloat($$$.toString())", "(s)-> s!=null ?Float.parseFloat(s): null"));
        mapConvert_StringToValue.put(float.class, MethodConvert.i(float.class, "Float.parseFloat($$$.toString())", "(s)-> s!=null ?Float.parseFloat(s): 0f"));

        mapConvert_StringToValue.put(Boolean.class, MethodConvert.i(Boolean.class, "Boolean.parseBoolean($$$.toString())", "(s)-> s!=null ? Boolean.parseBoolean(s): null"));
        mapConvert_StringToValue.put(boolean.class, MethodConvert.i(boolean.class, "Boolean.parseBoolean($$$.toString())", "(s)->s!=null ? Boolean.parseBoolean(s) : false"));
        mapConvert_StringToValue.put(Character.class, MethodConvert.i(Character.class, "$$$.isString().stringValue().charAt(0)", "(s)->s!=null ? Character.valueOf(s): null"));
        mapConvert_StringToValue.put(char.class, MethodConvert.i(char.class, "$$$.isString().stringValue().charAt(0)", "(s)-> s!=null ? Character.valueOf(s) : ''"));

        //Special Case / Special Handling
    }

    MethodConvert<Object> methodConvert_ofPojo = MethodConvert.i(Object.class, "$$$.isObject().toString()", "(s)-> s!=null ? $$$ : ''");

    public static List<Class<?>> listClass_SupportedContainers = Arrays.asList(List.class, Map.class);
    //endregion


    //region Methods Setters
    public CodeGenerator() {
    }

    public CodeGenerator setPrintLogInfo(boolean printLogInfo) {
        isPrintLogInfo = printLogInfo;
        return this;
    }

    public CodeGenerator setGeneratedPackageName(String generatedPackageName) {
        this.generatedPackageName = generatedPackageName;
        return this;
    }

    public CodeGenerator setGeneratedClassNameSingle(String generatedClassNameSingle) {
        this.generatedClassNameSingle = generatedClassNameSingle;
        return this;
    }

    public CodeGenerator setExportOnlyExpose(boolean exportOnlyExpose) {
        isExportOnlyExpose = exportOnlyExpose;
        return this;
    }

    public CodeGenerator setFilePathOfGeneratedFiles(File filePathToGenerated) {
        this.filePathToGenerated = filePathToGenerated;
        return this;
    }
    //endregion

    //region Filters & Lists
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
                if (annot != null)
                    fieldsFiltered.add(field);
            } else {
                //Adding even if it is not supported (in order to add comment)
                fieldsFiltered.add(field);
            }
        }
        return fieldsFiltered;
    }
    //endregion

    //region Methods To Json

    /**
     * Generate method "toJson(Pojo)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param cI, the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public static CodeBlock generatedCode_ToJson_Method(ClassInfo cI) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();

        codeBlock.addStatement("return \"TODO\"");
        return codeBlock.build();
    }
    //endregion

    //region Method From Json

    /**
     * Generate method "fromJson(Class, String)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param cI, the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public CodeBlock generateCode_FromJson_Method(ClassInfo cI) {
        Utilities.log.info("Class [" + cI.getClassToSerialize() + "]");

        List<FieldInfo> listFields = cI.getListFieldInfo();

        CodeBlock.Builder codeBlock = CodeBlock
                .builder()
                .addStatement("$T jsonValue = $T.parseStrict(json)", classJsonValue, classJsonParser)
                .addStatement("$T jsonObject = jsonValue.isObject()", classJsonObject)
                .beginControlFlow("\r\nif (jsonObject != null) ")
                .addStatement("$T structure = new $T()", cI.getClassToSerialize(), cI.getClassToSerialize());

        //For ALL exposed fields
        listFields.forEach((field -> {
            codeBlock.add(generateCode_FromJson_SetField(field));
        }));

        codeBlock
                .addStatement("return structure")
                .endControlFlow()
                .addStatement("return null");

        return codeBlock.build();
    }


    /**
     * Generate code to set field of a pojo.
     * Check the type of the field (primitive, container, pojo, enum)
     * and generate the correct setXXX(....)
     *
     * @param fI, the fieldInfo to set
     * @return the generated code block
     */
    public CodeBlock generateCode_FromJson_SetField(FieldInfo fI) {
        Utilities.log.info("Preparing Field [" + fI.nameField + "] type [" + fI.fieldClass + "]");

        CodeBlock.Builder codeBlockWrapper = CodeBlock.builder();
        CodeBlock.Builder codeBlockAssignment = null;

        boolean isImplemented = true;
        if (fI.isSupportedPrimitive(mapConvert_StringToValue)) {

            /*
             * A. Case Primitive Type
             */
            /*Generated [structure.setFieldName(SerializerGWT.toString(jsonObject.get("string")))]*/

            //Using Inline function of JSON to Primitive
            String convertCode = mapConvert_StringToValue.get(fI.fieldClass).inlineFromStringToClass(fI.jsonObjGet());
            if (convertCode == null) {
                convertCode = "//Not implemented. Consider to add it manually or ignore";
            }

            codeBlockAssignment = CodeBlock.builder().addStatement("structure.set" + fI.nameUpper() + "(" + convertCode + ")", classUtilities);

        } else if (fI.isPojo(listClass_Generated) != null) {

            /*
             * B. Pojo, the field is another pojo, use a 'fromJson(String)' to convert it.
             */

            ClassInfo cI_of_Field = fI.isPojo(listClass_Generated);
            String convertCode = cI_of_Field.methodFromJson(methodConvert_ofPojo.inlineFromStringToClass(fI.jsonObjGet()));
            codeBlockAssignment = CodeBlock.builder().addStatement("structure.set" + fI.nameUpper() + "(" + convertCode + ")", classUtilities);

        } else if (fI.isSupportedContainer(listClass_SupportedContainers)) {

            /*
             * C. Case Container of Primitive or Supported Types
             */

            //Using lamdba function to convert containers. like: structure.setListString(SerializerGWT.toList(jsonObject.get("listString"), (s)->s.toString()));
            PairStructure<CodeBlock.Builder, Boolean> pair = generatedCode_FromJson_SetField_Container(fI);
            codeBlockAssignment = pair.getFirst();
            isImplemented = pair.getSecond();

        } else if (fI.fieldClass.isEnum()) {

            /*
             * C. Case Enum
             */

            String convertCode = mapConvert_StringToValue.get(String.class).inlineFromStringToClass(fI.jsonObjGet());
            codeBlockAssignment = CodeBlock.builder().addStatement(fI.structureSet("$T.valueOf(" + convertCode + ")"), fI.fieldClass);

        } else {

            /*
             * D. Not Supported
             */

            codeBlockWrapper.addStatement(CodeGeneratorUtilities.toComment_Warning(fI, "Not supported type"));
            isImplemented = false;
        }

        if (codeBlockAssignment != null) {
            if (isPrintLogInfo) {
                //Move log.info as first line before assignment
                codeBlockAssignment = CodeBlock.builder()
                        .addStatement("log.info(\"Field:" + fI.nameField + ":\"+" + fI.jsonObjGet() + ")")
                        .add(codeBlockAssignment.build());
            }
            if (isImplemented)
                generateCode_wrapIfNotNull_JSONValue(codeBlockWrapper, fI, codeBlockAssignment.build());
            else
                codeBlockWrapper.add(codeBlockAssignment.build());
        }

        return codeBlockWrapper.build();
    }

    public void generateCode_wrapIfNotNull_JSONValue(CodeBlock.Builder codeBlock, FieldInfo fieldI, CodeBlock codeBlockToWrap) {
        if (isCheckNotNullTheJsonValue_BeforeSetJavaField)
            codeBlock.beginControlFlow("if(" + fieldI.jsonObjGet() + "!=null)");

        codeBlock.add(codeBlockToWrap);

        if (isCheckNotNullTheJsonValue_BeforeSetJavaField)
            codeBlock.endControlFlow();
    }
    //endregion

    //region Methods From Json (container related)

    /**
     * Generate Code that converts From json/string to Container.
     * The type-arguments must be checked for Primitives OR Pojo's and use the correct lamda methods
     *
     * @param fI, the field info of the container
     * @return generated code that converts json to container
     */
    public PairStructure<CodeBlock.Builder, Boolean> generatedCode_FromJson_SetField_Container(FieldInfo fI) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        boolean isImplemented = true;
        if (fI.field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType typeGeneric = (ParameterizedType) fI.field.getGenericType();
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fI.fieldClass == List.class) {

                //structure.setMember( SerializerGWT.toList( jsonObject.get("listString"), (s)->s.toString()) );
                //mapConvert_StringToValue.get((Class<?>) arguments[0]).funcFromStringToClass;
                String methodConvert_item = generateCode_LamdaFromStringToClass((Class<?>) arguments[0]);
                codeBlock.addStatement(fI.structureSet("$T.toListFromS(" + fI.jsonObjGet() + "," + methodConvert_item + ")"), classUtilities);

            } else if (fI.fieldClass == Map.class) {

                //String methodConvert_key = mapConvert_StringToValue.get((Class<?>) arguments[0]).funcFromStringToClass;
                //String methodConvert_value = mapConvert_StringToValue.get((Class<?>) arguments[1]).funcFromStringToClass;
                System.out.println("-----------------0");
                System.out.println(arguments[0]);
                System.out.println("-----------------1");
                System.out.println(arguments[1]);

                String methodConvert_key = generateCode_LamdaFromStringToClass((Class<?>) arguments[0]);
                String methodConvert_value = generateCode_LamdaFromStringToClass((Class<?>) arguments[1]);
                codeBlock.addStatement(fI.structureSet("$T.toMapFromS(" + fI.jsonObjGet() + "," + methodConvert_key + "," + methodConvert_value + ")"), classUtilities);

            } else {
                //Not Supported Info
                codeBlock.addStatement(CodeGeneratorUtilities.toComment_Warning(fI, "Not type parameters in container"));
                isImplemented = false;
            }

        } else {

            codeBlock.addStatement(CodeGeneratorUtilities.toComment_Warning(fI, "Not supported container"));
            isImplemented = false;

        }

        return PairStructure.n(codeBlock, isImplemented);
    }

    public String generateCode_LamdaFromStringToClass(Class<?> fieldType) {
        if (mapConvert_StringToValue.containsKey(fieldType)) {
            //1. The type is "Primitive", find the Function<String,K> lamda function to use

            return mapConvert_StringToValue.get(fieldType).funcFromStringToClass;
        } else {
            List<ClassInfo> cI_list = listClass_Generated.stream().filter((cI) -> cI.getClassToSerialize() == fieldType).collect(Collectors.toList());

            //2. The type is "Object", find the Function<String,K> lamda function to use
            if (cI_list.size() == 1) {
                ClassInfo cI = cI_list.get(0);
                String convertCode = "(s) -> " + cI.methodFromJson("s");
                return convertCode;
            }
        }
        return null;
    }

    //endregion

    public List<TypeSpec> generate_JavaCode(Set<Class<?>> setClasses) throws IOException {

        List<TypeSpec> listTypeBuilder = new ArrayList<>();

        /*
         * A. Create Class
         */
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(this.generatedClassNameSingle).addModifiers(Modifier.PUBLIC);

        /*
         * B. First Pass to gather ALL the generated classes and track the serializing methods
         */
        setClasses.forEach((classToSerialize) -> {
            List<FieldInfo> listFieldInfo = listFields_OfClass(classToSerialize, this.isExportOnlyExpose).stream().map(FieldInfo::new).collect(Collectors.toList());
            if (listFieldInfo.size() > 0) {
                listClass_Generated.add(new ClassInfo(classToSerialize, listFieldInfo));
            }
        });

        /*
         * Set log, and date converters
         */
        CodeGeneratorUtilities.addHelperMethods(this, classBuilder);

        /*
         * C. For each class Generated the "from Json"
         */
        List<MethodSpec> listMethodsFromJson = new ArrayList<>();
        List<MethodSpec> listMethodsToJson = new ArrayList<>();
        listClass_Generated.forEach((classInfo) -> {

            //The Class to serialize
            Class<?> classToSerialize = classInfo.getClassToSerialize();

            if (classInfo.getListFieldInfo().size() > 0) {
                MethodSpec method_convertFromJson = MethodSpec
                        .methodBuilder(classInfo.getMethodFromJson())
                        .addParameter(String.class, "json")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(classToSerialize)
                        .addCode(generateCode_FromJson_Method(classInfo))
                        .build();

                MethodSpec method_convertToJson = MethodSpec
                        .methodBuilder(classInfo.getMethodToJson())
                        .addParameter(classToSerialize, "structure")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(String.class)
                        .addCode(generatedCode_ToJson_Method(classInfo))
                        .build();

                listMethodsFromJson.add(method_convertFromJson);
                listMethodsToJson.add(method_convertToJson);
            }

        });

        classBuilder.addJavadoc("Generated for total " + setClasses.size() + " structures \r\n\r\n");
        for (ClassInfo cI : listClass_Generated) {
            classBuilder.addJavadoc("" + cI.getClassToSerialize().getName() + " [fields:" + cI.getListFieldInfo().size() + "/" + cI.getClassToSerialize().getDeclaredFields().length + "] \r\n");
        }


        /*
         * D. Add Methods "from/to Json"
         */
        for (int i = 0; i < listMethodsFromJson.size(); i++) {
            classBuilder.addMethod(listMethodsFromJson.get(i));
            classBuilder.addMethod(listMethodsToJson.get(i));
        }

        listTypeBuilder.add(classBuilder.build());
        return listTypeBuilder;
    }

    public List<JavaFile> build(Set<Class<?>> setClasses) throws IOException {
        List<JavaFile> listJavaFile = new ArrayList<>();

        List<TypeSpec> listSpecs = generate_JavaCode(setClasses);
        listSpecs.add(CodeGeneratorUtilities.generate_javaCodeTypes(this, this.generatedClassNameSingle + "Types"));

        /*
         * E. Export
         */
        for (TypeSpec listSpec : listSpecs) {
            JavaFile javaFile = JavaFile
                    .builder(generatedPackageName, listSpec)
                    .build();

            if (filePathToGenerated != null) {
                if (filePathToGenerated.isDirectory())
                    javaFile.writeTo(filePathToGenerated);
                else
                    throw new RuntimeException("Ensure that the exported path is directory [" + filePathToGenerated.getAbsolutePath() + "]");
            }
        }

        return listJavaFile;
    }
}

