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

public class CodeGenJsonFrom {

    /**
     * Generate method "fromJson(Class, String)" of the input ClassInfo
     * Loop all the listed fields
     *
     * @param method, the method spec
     * @param cI,     the input ClassInfo to create the method
     * @return the generated code-block of the method
     */
    public static MethodSpec.Builder generateCode_FromJson_Method(CodeGenerator cg, MethodSpec.Builder method, ClassInfo cI) {
        Utilities.log.info("Class From JSON [" + cI.getClassToSerialize() + "]");

        List<FieldInfo> listFields = cI.getListFieldInfo();
        method.addStatement("$T jsonValue = $T.parseStrict(json)", CodeGenerator.classJsonValue, CodeGenerator.classJsonParser).addStatement("$T jsonObject = jsonValue.isObject()", CodeGenerator.classJsonObject).beginControlFlow("\r\nif (jsonObject == null) ").addStatement("return null").endControlFlow();

        //Generate POJO (structure)
        method.addStatement("$T structure = new $T()", cI.getClassToSerialize(), cI.getClassToSerialize());
        //For ALL exposed fields
        listFields.forEach((fieldI -> {
            CodeGenJsonFrom.generateCode_FromJson_SetField(cg, method, fieldI);
        }));
        if (cg.isGeneratePostDeserializeIfExist) {
            if (CodeGenUtils.isMethodPresent(cI.getClassToSerialize(), CodeGenerator.Method_Name_toCall_After_Deserialize_of_Structure))
                method.addStatement("structure." + CodeGenerator.Method_Name_toCall_After_Deserialize_of_Structure + "()");
        }
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
    public static void generateCode_FromJson_SetField(CodeGenerator cg, MethodSpec.Builder method, FieldInfo fI) {
        Utilities.log.info("Preparing Field [" + fI.nameField + "] type [" + fI.fieldClass + "]");

        Class<?> classOfField = fI.fieldClass;
        ClassInfo cInfo_OfField = CodeGenerator.findClassInfoWithType(classOfField, cg.listClassInfo);
        boolean isSupported = MapConverter.MapConverters.containsKey(classOfField) || cInfo_OfField != null || classOfField.isEnum() || CodeGenerator.listSupportedContainers.contains(classOfField);

        // Always print debug is supported
        if (cg.isPrintLogInfo) {
            method.addStatement(CodeGenerator.code_toLog(CodeGenerator.quote("Field:" + fI.nameField + ":") + "+" + fI.jsonObjGet(), true));
        }

        // Return if it is not supported
        if (!isSupported) {

            if (cg.isGenerateCompileErrorOnNotSupported)
                method.addStatement(CodeGenerator.toComment_safe("TODO " + fI.toSimpleString() + " Warning, Not supported type, the field will be ignored."));
            else
                method.addComment(CodeGenerator.toComment_safe("TODO " + fI.toSimpleString() + " Warning, Not supported type, the field will be ignored."));

        } else {

            // Check if not null
            if (cg.isCheckNotNullValue_BeforeUse) method.beginControlFlow("if(" + fI.jsonObjGet() + "!=null)");

            if (MapConverter.MapConverters.containsKey(classOfField)) {

                /*
                 * A. Case Primitive Type
                 *  structure.setParameterName(Long.parse(parameterName))
                 */

                //Using Inline function of JSON to Primitive
                String convertCode = CodeGenerator.converterOf(fI.fieldClass).inline_JsonV_ToClass(fI.jsonObjGet());
                if (convertCode == null) {
                    if (CodeGenerator.isGenerateErrorForHeadsUp)
                        method.addCode(CodeGenerator.Error_Code_HeadsUp.replace("$XXX$", "(" + fI.toSimpleString() + ")"));
                    method.addComment("[" + fI.toSimpleString() + "]" + CodeGenerator.NOT_IMPLEMENTED);
                } else {
                    method.addStatement(fI.structureSet(convertCode), CodeGenerator.classUtilities);
                }


            } else if (cInfo_OfField != null) {

                /*
                 * B. Pojo, the field is another pojo, use a 'fromJson(String)' to convert it.
                 * structure.setΧΧΧ(SerGwtJson_Version.toDataVersion(jsonObject.get("version").isObject().toString()))
                 */

                String convertCode = cInfo_OfField.code_methodFromJson(MapConverter.MethodConvert_ofPojo.inline_JsonV_ToClass(fI.jsonObjGet()), true);
                method.addStatement(fI.structureSet(convertCode), CodeGenerator.classUtilities);

            } else if (fI.fieldClass.isEnum()) {

                /*
                 * C. Case Enum TODO check sample
                 * structure.setType(Enum.valueOf(parameterName.isString().stringValue())
                 */

                String convertCode = CodeGenerator.converterOf(String.class).inline_JsonV_ToClass(fI.jsonObjGet());
                method.addStatement(fI.structureSet(CodeGenerator.code_string_to_enum(convertCode)), fI.fieldClass);

            } else if (fI.isSupportedContainer(CodeGenerator.listSupportedContainers)) {

                /*
                 * D. Case Container of Primitive or Supported Types
                 */

                /* Using converter containers.
                   SerGwtUtils.toListPojo_JsonV_FuncS(
                            jsonObject.get("listDateSimple"),
                           (s)->SerGwtUtils.toDateFromS(s)))
                 */
                PairStructure<String, Boolean> codePair = generateCode_FromJson_SetField_Container(
                        cg,
                        fI.field.getGenericType(),
                        fI.jsonObjGet(),
                        "[Member:" + fI.nameField + "/ Serializable:" + fI.nameSerializable + "] (" + fI.field.getGenericType() + ")",
                        true,
                        0);

                if (codePair.getSecond())
                    method.addStatement(fI.structureSet(codePair.getFirst()));
                else
                    method.addComment(codePair.getFirst());
            } else {

            }

            if (cg.isCheckNotNullValue_BeforeUse) method.endControlFlow();
        }
    }

    /**
     * Generate Code that converts a JSONValue OR String to Parameterized Container with the input type.
     * The type-arguments must be checked for Primitives, Pojo's, Enum or Containers and use the needed converted methods
     * <p>
     * SerGwtUtils.toListPojo_JsonV_FuncS(
     * jsonObject.get("listDateSimple"),
     * (s)->SerGwtUtils.toDateFromS(s)))
     * )
     *
     * @param type,                  the field type
     * @param paramName,             the param name to be used  (convertMethod(paramName) )
     * @param fieldName_ForComments, the field info of the container
     * @param isParamJsonValue,      If true the return converted methods expect 'JSONValue' otherwise expect 'String'
     * @param iterationIndex,        the field info of the container
     * @return generated code that converts json to container
     */
    @SuppressWarnings("Duplicates")
    public static PairStructure<String, Boolean> generateCode_FromJson_SetField_Container(CodeGenerator cg, Type type, String paramName, String fieldName_ForComments, boolean isParamJsonValue, int iterationIndex) {

        if (type instanceof ParameterizedType) {

            ParameterizedType typeGeneric = (ParameterizedType) type;
            Class<?> fieldClass = (Class<?>) typeGeneric.getRawType();
            Type[] arguments = typeGeneric.getActualTypeArguments();

            if (fieldClass == List.class) {

                String methodConvert_item = cg.generateCode_Lamda_Converter(cg.Convert_String_ToClass, arguments[0], iterationIndex);
                return CodeGenerator.code(CodeGenerator.code_toListPojo_fromV_methodS(isParamJsonValue, paramName, methodConvert_item));

            } else if (fieldClass == Map.class) {

                String methodConvert_key = cg.generateCode_Lamda_Converter(cg.Convert_String_ToClass, arguments[0], iterationIndex);
                String methodConvert_value = cg.generateCode_Lamda_Converter(cg.Convert_String_ToClass, arguments[1], iterationIndex);

                return CodeGenerator.code(CodeGenerator.code_toMapPojo_fromV_methodS(isParamJsonValue, paramName, methodConvert_key, methodConvert_value));

            } else {

                return CodeGenerator.comment("TODO " + fieldName_ForComments + " Not implemented container");

            }

        } else {
            return CodeGenerator.comment("TODO " + fieldName_ForComments + " Not type parameters in container (ignoring)");
        }

    }

}
