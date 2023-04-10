package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.model.ClassInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.HashMap;

public class CodeGenUtils {

    /**
     * Generate the name of the generated class.
     * 1. Just a single class for all the methods
     * OR
     * 2. Classes based on the package name
     *
     * @param classToSerialize param
     * @param cd               param
     * @return the class name
     */
    public static String generateClassNameOf(Class<?> classToSerialize, CodeGenerator cd) {
        if (cd.isGenerateSeparateClassPerPackage) {
            String packageName = classToSerialize.getPackage().getName();
            if (packageName.contains(".")) {
                packageName = packageName.substring(packageName.lastIndexOf('.') + 1);
            }
            packageName = Utilities.toUpperFirstLtr(packageName);
            if (cd.generatedClassName.contains("$"))
                return cd.generatedClassName.replace("$", packageName);
            else
                return cd.generatedClassName + "_" + packageName;
        } else {
            return cd.generatedClassName;
        }
    }

    /**
     * Generate A helper  class, with from/to methods in a single method based on the input class
     *
     * @param codeGenerator
     * @param className
     * @return
     */
    public static TypeSpec generate_javaCodeTypes(CodeGenerator codeGenerator,
                                                  String className,
                                                  boolean isGenerateFromJson,
                                                  boolean isGenerateToJson) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);

        //1. Add All serialized classes
        TypeName classType = ArrayTypeName.of(Class.class);
        CodeBlock.Builder codeBlockInitializer = CodeBlock.builder().add("new Class<?>[] {\n");
        for (ClassInfo classI : codeGenerator.listClassInfo)
            codeBlockInitializer.add("$T.class,\n", classI.getClassToSerialize());
        codeBlockInitializer.add("}");

        FieldSpec.Builder fieldSpec = FieldSpec.builder(classType, "classes", Modifier.PUBLIC, Modifier.STATIC);
        fieldSpec.initializer(codeBlockInitializer.build());
        classBuilder.addField(fieldSpec.build());

        //2. Add method "fromJson"
        if (isGenerateFromJson)
            classBuilder.addMethod(CodeGenUtils.code_FromJson_For_All_POJO_Method(codeGenerator));

        if (isGenerateToJson)
            classBuilder.addMethod(CodeGenUtils.code_ToJson_For_All_POJO_Method(codeGenerator));

        return classBuilder.build();
    }

    public static void addHelperMethods(CodeGenerator codeGenerator, TypeSpec.Builder classBuilder) {
        //Logging
        classBuilder.addField(FieldSpec.builder(java.util.logging.Logger.class, "log", Modifier.PUBLIC, Modifier.STATIC).initializer("Logger.getLogger(\"\")").build());

        boolean isExportDateMethds = false;
        /*
         * Currently Date methods reside in Standard utilities class
         * (Code remains as example)
         */
        if (isExportDateMethds) {
            //Date Formats
            classBuilder.addField(FieldSpec.builder(CodeGenerator.classDateFormat, "dateFormat_ISO8601", Modifier.PUBLIC, Modifier.STATIC).initializer("DateTimeFormat.getFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\")").build());
            classBuilder.addField(FieldSpec.builder(CodeGenerator.classDateFormat, "dateFormat_Original", Modifier.PUBLIC, Modifier.STATIC).initializer("DateTimeFormat.getFormat(\"MMM, d, yyyy hh:mm:ss a\")").build());

            /* public static Date toDate(JSONValue json) Method */
            classBuilder.addMethod(MethodSpec
                    .methodBuilder("toDateFromV")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(CodeGenerator.classJsonValue, "jsonDate")
                    .returns(Date.class)
                    .addCode(methodToDate_FromJsonValue, new HashMap<>()).build());

            /* public static Date toDate(String json) Method */
            classBuilder.addMethod(MethodSpec
                    .methodBuilder("toDateFromS")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(String.class, "jsonDate")
                    .returns(Date.class)
                    .addCode(methodToDate_FromString, new HashMap<>()).build());
        }
    }

    /**
     * Generate Method, [public static <T> T fromJson(Class<T>, String){}]
     */
    private static MethodSpec code_FromJson_For_All_POJO_Method(CodeGenerator codeGenerator) {
        String parameterNameClass = "clazz";
        String parameterNameJson = "json";

        TypeVariableName typeT = TypeVariableName.get("T");
        ParameterSpec classParameter = ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class), typeT), parameterNameClass).build();

        //Method Spec
        MethodSpec.Builder method = MethodSpec
                .methodBuilder("fromJson")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeT)
                .addParameter(classParameter)
                .addParameter(String.class, parameterNameJson)
                .returns(TypeVariableName.get("T"));

        //Method Code
        CodeBlock.Builder builder = CodeBlock.builder();
        for (ClassInfo classI : codeGenerator.listClassInfo) {
            builder.beginControlFlow("if(" + parameterNameClass + "==$T.class)", classI.getClassToSerialize());
            builder.addStatement(
                    "return (T)$T." + classI.code_methodFromJson(parameterNameJson, false),
                    ClassName.get(codeGenerator.generatedPackageName, classI.getGeneratedClassName()));
            builder.endControlFlow();
        }

        builder.addStatement("return null");
        method.addCode(builder.build());


        return method.build();
    }

    private static MethodSpec code_ToJson_For_All_POJO_Method(CodeGenerator codeGenerator) {
        String parameterNameStructure = "structure";

        //Method Spec
        MethodSpec.Builder method = MethodSpec
                .methodBuilder("toJson")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Object.class, parameterNameStructure)
                .returns(String.class);

        //Method Code
        CodeBlock.Builder builder = CodeBlock.builder();

        //Check if not null
        builder.beginControlFlow("if(" + parameterNameStructure + " == null)");
        builder.addStatement("return null");
        builder.endControlFlow();

        for (ClassInfo classI : codeGenerator.listClassInfo) {
            builder.beginControlFlow("if(" + parameterNameStructure + " instanceof $T)", classI.getClassToSerialize());
            builder.addStatement(
                    "return $T." + classI.code_methodToJson("($T)" + parameterNameStructure, false) + ".toString()",
                    ClassName.get(codeGenerator.generatedPackageName, classI.getGeneratedClassName()),
                    classI.getClassToSerialize()
            );
            builder.endControlFlow();
        }

        builder.addStatement("return null");
        method.addCode(builder.build());


        return method.build();
    }

    static String methodToDate_FromString
            = "if(jsonDate == null)\n" +
            "    return null;\n" +
            " //the toString() add quotes\n" +
            " jsonDate = jsonDate.replaceAll(\"\\\"\",\"\");\n" +
            "try {\n" +
            "    return new java.util.Date(Long.parseLong(jsonDate));\n" +
            "} catch (Exception ignored) {}\n" +
            "try {\n" +
            "    return dateFormat_ISO8601.parse(jsonDate);\n" +
            "} catch (Exception ignored){}\n" +
            "return dateFormat_Original.parse(jsonDate);\n";

    static String methodToDate_FromJsonValue
            = "if (jsonDate == null)\n" +
            "    return null;\n" +
            "if (jsonDate.isNumber() != null)\n" +
            "    try {\n" +
            "        return new java.util.Date((long) jsonDate.isNumber().doubleValue());\n" +
            "    } catch (Exception ignored) {\n" +
            "    }\n" +
            "if (jsonDate.isString() != null) {\n" +
            "    try {\n" +
            "        return dateFormat_ISO8601.parse(jsonDate.isString().stringValue());\n" +
            "    } catch (Exception ignored) {\n" +
            "    }\n" +
            "    return dateFormat_Original.parse(jsonDate.isString().stringValue());\n" +
            "}\n" +
            "throw new RuntimeException(\"Unable to convert date from json \"+jsonDate);\n";


}
