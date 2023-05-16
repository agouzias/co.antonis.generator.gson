package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import co.antonis.generator.model.sample.PojoType;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import java.lang.String;
import java.util.logging.Logger;

/**
 * Generated for total 2 structures
 *
 * @see co.antonis.generator.model.sample.PojoParent [fields:22/22]
 * @see co.antonis.generator.model.sample.PojoSimple [fields:4/4]
 */
public class SerGwtJson_Sample {
    public static Logger log = Logger.getLogger("");

    static {
        JSONString ignore1;
        JSONBoolean ignore2;
        JSONNumber ignore3;
    }

    public static PojoParent toPojoParent(String json) {
        JSONValue jsonValue = JSONParser.parseStrict(json);
        JSONObject jsonObject = jsonValue.isObject();

        if (jsonObject == null) {
            return null;
        }
        PojoParent structure = new PojoParent();
        if (jsonObject.get("string") != null) {
            structure.setString(jsonObject.get("string").isString().stringValue());
        }
        if (jsonObject.get("mapString") != null) {
            structure.setMapString(SerGwtUtils.toMapPojo_JsonV_FuncS(
                    jsonObject.get("mapString"),
                    (s) -> s != null ? Integer.parseInt(s) : null,
                    (s) -> s)
            );
        }
        if (jsonObject.get("stringNotSet") != null) {
            structure.setStringNotSet(jsonObject.get("stringNotSet").isString().stringValue());
        }
        if (jsonObject.get("nameOfType") != null) {
            structure.setPojoType(PojoType.valueOf(jsonObject.get("nameOfType").isString().stringValue()));
        }
        if (jsonObject.get("isBooleanValue") != null) {
            structure.setBooleanValue(Boolean.parseBoolean(jsonObject.get("isBooleanValue").toString()));
        }
        if (jsonObject.get("date") != null) {
            structure.setDate(SerGwtUtils.toDateFromV(jsonObject.get("date")));
        }
        if (jsonObject.get("listString") != null) {
            structure.setListString(SerGwtUtils.toListPojo_JsonV_FuncS(
                    jsonObject.get("listString"),
                    (s) -> s)
            );
        }
        if (jsonObject.get("listDouble") != null) {
            structure.setListDouble(SerGwtUtils.toListPojo_JsonV_FuncS(
                    jsonObject.get("listDouble"),
                    (s) -> s != null ? Double.parseDouble(s) : null)
            );
        }
        if (jsonObject.get("listLong") != null) {
            structure.setListLong(SerGwtUtils.toListPojo_JsonV_FuncS(
                    jsonObject.get("listLong"),
                    (s) -> s != null ? Long.parseLong(s) : null)
            );
        }
        if (jsonObject.get("listMapChild") != null) {
            structure.setListMapChild(SerGwtUtils.toListPojo_JsonV_FuncS(
                            jsonObject.get("listMapChild"),
                            (s0) -> SerGwtUtils.toMapPojo_String_FuncS(
                                    s0,
                                    (s) -> s != null ? Integer.parseInt(s) : null,
                                    (s1) -> SerGwtUtils.toListPojo_String_FuncS(
                                            s1,
                                            (s) -> SerGwtJson_Sample.toPojoSimple(s))
                            )
                    )
            );
        }
        if (jsonObject.get("listPojoType") != null) {
            structure.setListPojoType(SerGwtUtils.toListPojo_JsonV_FuncS(
                    jsonObject.get("listPojoType"),
                    (s) -> s != null ? co.antonis.generator.model.sample.PojoType.valueOf(s) : null)
            );
        }
        if (jsonObject.get("child") != null) {
            structure.setSimple(SerGwtJson_Sample.toPojoSimple(jsonObject.get("child").isObject().toString()));
        }
        if (jsonObject.get("charA") != null) {
            structure.setCharA(jsonObject.get("charA").isString().stringValue().charAt(0));
        }
        if (jsonObject.get("idL") != null) {
            structure.setNumberLongClass(Long.parseLong(jsonObject.get("idL").toString()));
        }
        if (jsonObject.get("listAny") != null) {
            // TODO [Member:listAny/ Serializable:listAny] (interface java.util.List) Not type parameters in container (ignoring)
        }
        if (jsonObject.get("numberInt") != null) {
            structure.setNumberInt(Integer.parseInt(jsonObject.get("numberInt").toString()));
        }
        if (jsonObject.get("numberDouble") != null) {
            structure.setNumberDouble(Double.parseDouble(jsonObject.get("numberDouble").toString()));
        }
        if (jsonObject.get("charNotSet") != null) {
            structure.setCharNotSet(jsonObject.get("charNotSet").isString().stringValue().charAt(0));
        }
        if (jsonObject.get("mapChild") != null) {
            structure.setMapChild(SerGwtUtils.toMapPojo_JsonV_FuncS(
                    jsonObject.get("mapChild"),
                    (s) -> s != null ? Integer.parseInt(s) : null,
                    (s) -> SerGwtJson_Sample.toPojoSimple(s))
            );
        }
        if (jsonObject.get("boolValue") != null) {
            structure.setBoolValue(Boolean.parseBoolean(jsonObject.get("boolValue").toString()));
        }
        if (jsonObject.get("mapListChild") != null) {
            structure.setMapListChild(SerGwtUtils.toMapPojo_JsonV_FuncS(
                            jsonObject.get("mapListChild"),
                            (s) -> s != null ? Integer.parseInt(s) : null,
                            (s0) -> SerGwtUtils.toListPojo_String_FuncS(
                                    s0,
                                    (s) -> SerGwtJson_Sample.toPojoSimple(s))
                    )
            );
        }
        if (jsonObject.get("id") != null) {
            structure.setNumberLong(Long.parseLong(jsonObject.get("id").toString()));
        }
        return structure;
    }

    public static JSONObject fromPojoParent(PojoParent structure) {
        if (structure == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        if (structure.getString() != null) {
            jsonObject.put("string", new JSONString(structure.getString()));
        }
        if (structure.getMapString() != null) {
            jsonObject.put("mapString", SerGwtUtils.toMapJson_FuncJ_FuncS(
                    structure.getMapString(),
                    (num) -> num != null ? Integer.toString(num) : null,
                    (str) -> (str != null) ? new JSONString(str) : null)
            );
        }
        if (structure.getStringNotSet() != null) {
            jsonObject.put("stringNotSet", new JSONString(structure.getStringNotSet()));
        }
        if (structure.getPojoType() != null) {
            jsonObject.put("nameOfType", new JSONString(structure.getPojoType().name()));
        }
        jsonObject.put("isBooleanValue", JSONBoolean.getInstance(structure.isBooleanValue()));
        if (structure.getDate() != null) {
            jsonObject.put("date", SerGwtUtils.toVFromDate(structure.getDate()));
        }
        if (structure.getListString() != null) {
            jsonObject.put("listString", SerGwtUtils.toListJson_FuncJ(
                    structure.getListString(),
                    (str) -> (str != null) ? new JSONString(str) : null)
            );
        }
        if (structure.getListDouble() != null) {
            jsonObject.put("listDouble", SerGwtUtils.toListJson_FuncJ(
                    structure.getListDouble(),
                    (num) -> num != null ? new JSONNumber(num) : null)
            );
        }
        if (structure.getListLong() != null) {
            jsonObject.put("listLong", SerGwtUtils.toListJson_FuncJ(
                    structure.getListLong(),
                    (num) -> num != null ? new JSONNumber(num) : null)
            );
        }
        if (structure.getListMapChild() != null) {
            jsonObject.put("listMapChild", SerGwtUtils.toListJson_FuncJ(
                            structure.getListMapChild(),
                            (pojo0) -> SerGwtUtils.toMapJson_FuncJ_FuncS(
                                    pojo0,
                                    (num) -> num != null ? Integer.toString(num) : null,
                                    (pojo1) -> SerGwtUtils.toListJson_FuncJ(
                                            pojo1,
                                            (pojo) -> SerGwtJson_Sample.fromPojoSimple(pojo))
                            )
                    )
            );
        }
        if (structure.getListPojoType() != null) {
            jsonObject.put("listPojoType", SerGwtUtils.toListJson_FuncJ(
                    structure.getListPojoType(),
                    (pojo) -> pojo != null ? new com.google.gwt.json.client.JSONString(pojo.name()) : null)
            );
        }
        if (structure.getSimple() != null) {
            jsonObject.put("child", SerGwtJson_Sample.fromPojoSimple(structure.getSimple()));
        }

        //[TODO - Heads UP, Null Converter or Other]
        // [[charA][char]]Not implemented. Consider to add it manually or ignore field
        if (structure.getNumberLongClass() != null) {
            jsonObject.put("idL", new JSONNumber(structure.getNumberLongClass()));
        }
        if (structure.getListAny() != null) {
            // TODO [Member:].. Not type parameters in container (ignoring)
        }
        jsonObject.put("numberInt", new JSONNumber(structure.getNumberInt()));
        jsonObject.put("numberDouble", new JSONNumber(structure.getNumberDouble()));

        //[TODO - Heads UP, Null Converter or Other]
        // [[charNotSet][char]]Not implemented. Consider to add it manually or ignore field
        if (structure.getMapChild() != null) {
            jsonObject.put("mapChild", SerGwtUtils.toMapJson_FuncJ_FuncS(
                    structure.getMapChild(),
                    (num) -> num != null ? Integer.toString(num) : null,
                    (pojo) -> SerGwtJson_Sample.fromPojoSimple(pojo))
            );
        }
        jsonObject.put("boolValue", JSONBoolean.getInstance(structure.isBoolValue()));
        if (structure.getMapListChild() != null) {
            jsonObject.put("mapListChild", SerGwtUtils.toMapJson_FuncJ_FuncS(
                            structure.getMapListChild(),
                            (num) -> num != null ? Integer.toString(num) : null,
                            (pojo0) -> SerGwtUtils.toListJson_FuncJ(
                                    pojo0,
                                    (pojo) -> SerGwtJson_Sample.fromPojoSimple(pojo))
                    )
            );
        }
        if (structure.getNumberLong() != null) {
            jsonObject.put("id", new JSONNumber(structure.getNumberLong()));
        }
        return jsonObject;
    }

    public static PojoSimple toPojoSimple(String json) {
        JSONValue jsonValue = JSONParser.parseStrict(json);
        JSONObject jsonObject = jsonValue.isObject();

        if (jsonObject == null) {
            return null;
        }
        PojoSimple structure = new PojoSimple();
        if (jsonObject.get("dateSimple") != null) {
            structure.setDateSimple(SerGwtUtils.toDateFromV(jsonObject.get("dateSimple")));
        }
        if (jsonObject.get("listDateSimple") != null) {
            structure.setListDateSimple(SerGwtUtils.toListPojo_JsonV_FuncS(
                    jsonObject.get("listDateSimple"),
                    (s) -> SerGwtUtils.toDateFromS(s))
            );
        }
        if (jsonObject.get("intSimple") != null) {
            structure.setIntSimple(Integer.parseInt(jsonObject.get("intSimple").toString()));
        }
        if (jsonObject.get("stringSimple") != null) {
            structure.setStringSimple(jsonObject.get("stringSimple").isString().stringValue());
        }
        return structure;
    }

    public static JSONObject fromPojoSimple(PojoSimple structure) {
        if (structure == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        if (structure.getDateSimple() != null) {
            jsonObject.put("dateSimple", SerGwtUtils.toVFromDate(structure.getDateSimple()));
        }
        if (structure.getListDateSimple() != null) {
            jsonObject.put("listDateSimple", SerGwtUtils.toListJson_FuncJ(
                    structure.getListDateSimple(),
                    (pojo) -> SerGwtUtils.toVFromDate(pojo))
            );
        }
        jsonObject.put("intSimple", new JSONNumber(structure.getIntSimple()));
        if (structure.getStringSimple() != null) {
            jsonObject.put("stringSimple", new JSONString(structure.getStringSimple()));
        }
        return jsonObject;
    }
}
