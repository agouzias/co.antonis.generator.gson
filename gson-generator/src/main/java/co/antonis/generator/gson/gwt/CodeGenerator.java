package co.antonis.generator.gson.gwt;

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

        mapConvert_StringToValue.put(Object.class, MethodConvert.i(Object.class, "$$$.asObject().toString()", "(s)-> s!=null ? $$$ : ''"));
    }

    public static List<Class<?>> ListClass_SupportedContainers = Arrays.asList(List.class, Map.class);
    //endregion


    //region Methods Setters
    CodeGenerator() {
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

    //region Code Generation


    public static CodeBlock generatedCode_ToJson_Method(Class<?> clazz, List<FieldInfo> listFields) {
        CodeBlock.Builder codeBlock = CodeBlock
                .builder();

        codeBlock.addStatement("return \"TODO\"");
        return codeBlock.build();
    }

    /*
     * Code Block
     */
    public CodeBlock generateCode_FromJson_Method(Class<?> clazz, List<FieldInfo> listFields) {

        CodeBlock.Builder codeBlock = CodeBlock
                .builder()
                .addStatement("$T jsonValue = $T.parseStrict(json)", classJsonValue, classJsonParser)
                .addStatement("$T jsonObject = jsonValue.isObject()", classJsonObject)
                .beginControlFlow("\r\nif (jsonObject != null) ")
                .addStatement("$T structure = new $T()", clazz, clazz);

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

    public static PairStructure<CodeBlock.Builder,Boolean> generatedCode_FromJson_SetField_Container(FieldInfo fI) {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        boolean isImplemented = true;
        if (fI.field.getGenericType() instanceof ParameterizedType typeGeneric) {
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fI.fieldClass == List.class) {

                //structure.setMember( SerializerGWT.toList( jsonObject.get("listString"), (s)->s.toString()) );
                String methodConvert_item = mapConvert_StringToValue.get((Class<?>) arguments[0]).funcFromStringToClass;
                codeBlock.addStatement(fI.structureSet("$T.toListFromS(" + fI.jsonObjGet() + "," + methodConvert_item + ")"), classUtilities);

            } else if (fI.fieldClass == Map.class) {

                String methodConvert_key = mapConvert_StringToValue.get((Class<?>) arguments[0]).funcFromStringToClass;
                String methodConvert_value = mapConvert_StringToValue.get((Class<?>) arguments[1]).funcFromStringToClass;
                codeBlock.addStatement(fI.structureSet("$T.toMapFromS(" + fI.jsonObjGet() + "," + methodConvert_key + "," + methodConvert_value + ")"), classUtilities);

            } else {
                //Not Supported Info
                codeBlock.addStatement(to_warning(fI, "Not type parameters in container"));
                isImplemented = false;
            }

        } else {

            codeBlock.addStatement(to_warning(fI, "Not supported container"));
            isImplemented = false;

        }

        return PairStructure.n(codeBlock,isImplemented);
    }

    public static String to_warning(FieldInfo fI, String extra) {
        return "//TODO Warning, [" + fI.nameField + "][" + fI.fieldClass + "] NOT serialized " + (extra != null ? extra : "");
    }

    public CodeBlock generateCode_FromJson_SetField(FieldInfo fI) {
        CodeBlock.Builder codeBlockWrapper = CodeBlock.builder();
        CodeBlock.Builder codeBlockAssignment = null;

        boolean isImplemented = true;
        if (fI.isSupportedPrimitive()) {

            /*
             * A. Case Primitive Type
             */
            /*Generated [structure.setFieldName(SerializerGWT.toString(jsonObject.get("string")))]*/

            //Using Inline function of JSOV to Primitive
            String convertCode = mapConvert_StringToValue.get(fI.fieldClass).inline(fI.jsonObjGet());
            if (convertCode == null) {
                convertCode = "TODO ?";
            }

            codeBlockAssignment = CodeBlock.builder().addStatement("structure.set" + fI.nameUpper() + "(" + convertCode + ")", classUtilities);

        } else if (fI.isInPojo(listClass_Generated) != null) {

            ClassInfo cI_of_Field = fI.isInPojo(listClass_Generated);
            String convertToString = mapConvert_StringToValue.get(String.class).inline(fI.jsonObjGet());
            String convertCode = cI_of_Field.fromJson(convertToString);
            codeBlockAssignment = CodeBlock.builder().addStatement("structure.set" + fI.nameUpper() + "(" + convertCode + ")", classUtilities);

        } else if (fI.isSupportedContainer()) {

            /*
             * B. Case Container of Primitive or Supported Types
             */
            //structure.setListString(SerializerGWT.toList(jsonObject.get("listString"), (s)->s.toString()));
            PairStructure<CodeBlock.Builder,Boolean> pair = generatedCode_FromJson_SetField_Container(fI);
            codeBlockAssignment = pair.getFirst();
            isImplemented = pair.getSecond();

        } else if (fI.fieldClass.isEnum()) {

            /*
             * C. Case Enum
             */
            String convertCode = mapConvert_StringToValue.get(String.class).inline(fI.jsonObjGet());
            codeBlockAssignment = CodeBlock.builder().addStatement(fI.structureSet("$T.valueOf(" + convertCode + ")"), fI.fieldClass);

        } else {

            /*
             * D. Not Supported
             */
            codeBlockWrapper.addStatement(to_warning(fI, "Not supported type"));
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
            List<FieldInfo> listFieldInfo = listFields_OfClass(classToSerialize, this.isExportOnlyExpose).stream().map(FieldInfo::new).toList();
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
                        .addCode(generateCode_FromJson_Method(classToSerialize, classInfo.getListFieldInfo()))
                        .build();

                MethodSpec method_convertToJson = MethodSpec
                        .methodBuilder(classInfo.getMethodToJson())
                        .addParameter(classToSerialize, "structure")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(String.class)
                        .addCode(generatedCode_ToJson_Method(classToSerialize, classInfo.getListFieldInfo()))
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

