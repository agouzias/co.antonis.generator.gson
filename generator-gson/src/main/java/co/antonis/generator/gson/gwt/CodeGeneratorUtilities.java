package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.model.ClassInfo;
import co.antonis.generator.gson.model.FieldInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Date;
import java.util.HashMap;

public class CodeGeneratorUtilities {

    public static TypeSpec generate_javaCodeTypes(CodeGenerator codeGenerator, String name) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);
        classBuilder.addMethod(CodeGeneratorUtilities.code_FromJson_For_All_POJO_Method(codeGenerator));
        return classBuilder.build();
    }

    public static void addHelperMethods(CodeGenerator codeGenerator, TypeSpec.Builder classBuilder) {
        //Logging
        classBuilder.addField(FieldSpec.builder(java.util.logging.Logger.class, "log", Modifier.PUBLIC, Modifier.STATIC).initializer("Logger.getLogger(\"\")").build());

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

    /**
     * Generate Method, [public static <T> T fromJson(Class<T>, String){}]
     */
    public static MethodSpec code_FromJson_For_All_POJO_Method(CodeGenerator codeGenerator) {
        String parameterNameClass = "clazz";
        String parameterNameJson = "json";

        TypeVariableName typeT = TypeVariableName.get("T");
        ParameterSpec classParameter = ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class), typeT), parameterNameClass).build();
        ClassName classNameOfGenerated = ClassName.get(codeGenerator.generatedPackageName, codeGenerator.generatedClassNameSingle);

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
        for (ClassInfo classI : codeGenerator.listClass_Generated) {
            builder.beginControlFlow("if(" + parameterNameClass + "==$T.class)", classI.getClassToSerialize());
            builder.addStatement("return (T)$T." + classI.code_methodFromJson(parameterNameJson), classNameOfGenerated);
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


    public static String toComment_safe(String comment) {
        return comment.replaceAll("\\$", "_");
    }

    public static String toComment_TODO(FieldInfo fI, String extra) {
        return toComment_safe("// TODO " + (fI != null ? fI.toSimpleString() : "") + " NOT serialized " + (extra != null ? extra : "") + "\r\n");
    }


}
