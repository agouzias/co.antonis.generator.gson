package co.antonis.generator.gson.gwt;

import co.antonis.generator.gson.Utilities;
import co.antonis.generator.gson.model.ClassInfo;
import co.antonis.generator.gson.model.FieldInfo;
import co.antonis.generator.gson.model.PairStructure;
import com.squareup.javapoet.MethodSpec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CodeGenJsonTo {


    /**
     * Generate method "toJson(Pojo)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param method, the method spec
     * @param cI,     the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public static MethodSpec.Builder generateCode_ToJson_Method(CodeGenerator cg, MethodSpec.Builder method, ClassInfo cI) {
        Utilities.log.info("Class To JSON [" + cI.getClassToSerialize() + "]");

        List<FieldInfo> listFields = cI.getListFieldInfo();
        method.beginControlFlow("if (structure == null) ").addStatement("return null").endControlFlow();

        //Generate JSONObject (jsonObject)
        method.addStatement("$T jsonObject = new $T()", CodeGenerator.classJsonObject, CodeGenerator.classJsonObject);
        //For ALL exposed fields
        listFields.forEach(fieldI -> {
            CodeGenJsonTo.generateCode_ToJson_SetField(cg, method, fieldI);
        });
        method.addStatement("return jsonObject");
        return method;
    }

    @SuppressWarnings("Duplicates")
    private static void generateCode_ToJson_SetField(CodeGenerator cg, MethodSpec.Builder method, FieldInfo fI) {
        Utilities.log.info("Preparing Field [" + fI.nameField + "] type [" + fI.fieldClass + "]");

        Class<?> classOfField = fI.fieldClass;
        ClassInfo cInfo_OfField = CodeGenerator.findClassInfoWithType(classOfField, cg.listClassInfo);
        boolean isSupported = MapConverter.MapConverters.containsKey(classOfField) || cInfo_OfField != null || classOfField.isEnum() || CodeGenerator.listSupportedContainers.contains(classOfField);

        // Always print debug is supported
        if (cg.isPrintLogInfo) {
            method.addStatement(CodeGenerator.code_toLog(CodeGenerator.quote("Field:" + fI.nameField + ":") + "+" + fI.structureGet(), true));
        }

        if (!isSupported) {

            method.addComment(CodeGenerator.toComment_safe("TODO " + fI.toSimpleString() + " Warning, Not supported type, the field will be ignored."));

        } else {

            // Check if not null
            if (cg.isCheckNotNullValue_BeforeUse && !fI.isPrimitive()) method.beginControlFlow("if(" + fI.structureGet() + "!=null)");

            if (MapConverter.MapConverters.containsKey(classOfField)) {

                /*
                 * A. Case Primitive Type
                 *  jsonObject.put("parameterName",new JSONString(structure.getParameterName()));
                 *  jsonObject.put("intSimple",new JSONNumber(structure.getIntSimple()));
                 */

                String convertCode = CodeGenerator.converterOf(classOfField).inline_Class_ToJsonV(fI.structureGet());
                if (convertCode == null)
                    method.addComment(CodeGenerator.NOT_IMPLEMENTED);
                else
                    method.addStatement(FieldInfo.jsonObjPut(fI.nameSerializable, convertCode));

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

                String convertCode = CodeGenerator.converterOf(String.class).inline_Class_ToJsonV(CodeGenerator.code_enum_to_string(fI.structureGet()));
                method.addStatement(FieldInfo.jsonObjPut(fI.nameSerializable, convertCode));

            } else if (fI.isSupportedContainer(CodeGenerator.listSupportedContainers)) {

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
                PairStructure<String, Boolean> codePair = generateCode_ToJson_SetField_Container(
                        cg,
                        fI.field.getGenericType(),
                        fI.structureGet(),
                        "[Member:]..",
                        true, 0
                );

                if (codePair.getSecond())
                    method.addStatement(fI.jsonObjPut(codePair.getFirst()));
                else
                    method.addComment(codePair.getFirst());
            }

            if (cg.isCheckNotNullValue_BeforeUse && !fI.isPrimitive()) method.endControlFlow();

        }
    }


    /**
     * SerializationGWTUtilities.toListJson_FuncJ(
     * structure.getListString(),
     * (s)->new JSONString(s)
     * )
     *
     * @param type,                  the field type
     * @param paramName,             the parameter name to be used
     * @param fieldName_ForComments, used for comments
     * @param isParamJsonValue,      todo
     * @param iterationIndex,        todo
     * @return generated code that converts pojo to json value
     */
    @SuppressWarnings("Duplicates")
    public static PairStructure<String, Boolean> generateCode_ToJson_SetField_Container(CodeGenerator gc, Type type, String paramName, String fieldName_ForComments, boolean isParamJsonValue, int iterationIndex) {

        if (type instanceof ParameterizedType) {

            ParameterizedType typeGeneric = (ParameterizedType) type;
            Class<?> fieldClass = (Class<?>) typeGeneric.getRawType();
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fieldClass == List.class) {

                String methodConvert_item = gc.generateCode_Lamda_Converter_StringToType_Or_TypeToJsonV(false, arguments[0], iterationIndex);
                return CodeGenerator.code(CodeGenerator.code_toListJson(paramName, methodConvert_item));

            } else if (fieldClass == Map.class) {

                String methodConvert_key = gc.generateCode_Lamda_Converter_StringToType_Or_TypeToJsonV(false, arguments[0], iterationIndex);
                String methodConvert_value = gc.generateCode_Lamda_Converter_StringToType_Or_TypeToJsonV(false, arguments[1], iterationIndex);

                return CodeGenerator.code(CodeGenerator.code_toMapJson(paramName, methodConvert_key, methodConvert_value));

            } else {

                return CodeGenerator.comment("TODO " + fieldName_ForComments + " Not implemented container");

            }

        } else {
            return CodeGenerator.comment("TODO " + fieldName_ForComments + " Not type parameters in container (ignoring)");
        }

    }

}
