package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.sub.PojoChild;
import com.google.gwt.json.client.JSONObject;
import java.util.logging.Logger;

/**
 * Generated for total 1 structures 
 * 
 * @see co.antonis.generator.model.sample.sub.PojoChild [fields:23/1] 
 */
public class SerializationGWTJson_Sub {
  public static Logger log = Logger.getLogger("");

  public static JSONObject fromPojoChild(PojoChild structure) {
    if (structure == null)  {
      return null;
    }
    JSONObject jsonObject = new JSONObject();
    if(structure.getString()!=null) {
      jsonObject.put("string",new JSONString(structure.getString()));
    }
    if(structure.getMapString()!=null) {
      jsonObject.put("mapString",SerializationGWTUtilities.toMapJson_FuncJ(
          structure.getMapString(),
          (num) -> new JSONNumber(num),
          (pojo) -> new JSONString(pojo))
          );
    }
    if(structure.getStringNotSet()!=null) {
      jsonObject.put("stringNotSet",new JSONString(structure.getStringNotSet()));
    }
    if(structure.getPojoType()!=null) {
      jsonObject.put("nameOfType",new JSONString(structure.getPojoType().name()));
    }
    jsonObject.put("isBooleanValue",JSONBoolean.getInstance(structure.isBooleanValue()));
    if(structure.getDate()!=null) {
      jsonObject.put("date",SerializationGWTUtilities.toVFromDate(structure.getDate()));
    }
    if(structure.getListString()!=null) {
      jsonObject.put("listString",SerializationGWTUtilities.toListJson_FuncJ(
          structure.getListString(),
          (pojo) -> new JSONString(pojo))
          );
    }
    if(structure.getListDouble()!=null) {
      jsonObject.put("listDouble",SerializationGWTUtilities.toListJson_FuncJ(
          structure.getListDouble(),
          (num) -> new JSONNumber(num))
          );
    }
    if(structure.getListLong()!=null) {
      jsonObject.put("listLong",SerializationGWTUtilities.toListJson_FuncJ(
          structure.getListLong(),
          (num) -> new JSONNumber(num))
          );
    }
    if(structure.getListMapChild()!=null) {
      jsonObject.put("listMapChild",SerializationGWTUtilities.toListJson_FuncJ(
          structure.getListMapChild(),
          (pojo0)->SerializationGWTUtilities.toMapJson_FuncJ(
          pojo0,
          (num) -> new JSONNumber(num),
          (pojo1)->SerializationGWTUtilities.toListJson_FuncJ(
          pojo1,
          (pojo) -> SerializationGWTJson_Sample.fromPojoSimple(pojo))
          )
          )
          );
    }
    jsonObject.put("numberB",new JSONNumber(structure.getNumberB()));
    if(structure.getListPojoType()!=null) {
      jsonObject.put("listPojoType",SerializationGWTUtilities.toListJson_FuncJ(
          structure.getListPojoType(),
          (pojo) -> pojo!=null ? new com.google.gwt.json.client.JSONString(pojo.name()) : null)
          );
    }
    if(structure.getSimple()!=null) {
      jsonObject.put("child",SerializationGWTJson_Sample.fromPojoSimple(structure.getSimple()));
    }
    // Not implemented. Consider to add it manually or ignore field
    if(structure.getNumberLongClass()!=null) {
      jsonObject.put("idL",new JSONNumber(structure.getNumberLongClass()));
    }
    if(structure.getListAny()!=null) {
      // TODO [Member:].. Not type parameters in container (ignoring)
    }
    jsonObject.put("numberInt",new JSONNumber(structure.getNumberInt()));
    jsonObject.put("numberDouble",new JSONNumber(structure.getNumberDouble()));
    // Not implemented. Consider to add it manually or ignore field
    if(structure.getMapChild()!=null) {
      jsonObject.put("mapChild",SerializationGWTUtilities.toMapJson_FuncJ(
          structure.getMapChild(),
          (num) -> new JSONNumber(num),
          (pojo) -> SerializationGWTJson_Sample.fromPojoSimple(pojo))
          );
    }
    jsonObject.put("boolValue",JSONBoolean.getInstance(structure.isBoolValue()));
    if(structure.getMapListChild()!=null) {
      jsonObject.put("mapListChild",SerializationGWTUtilities.toMapJson_FuncJ(
          structure.getMapListChild(),
          (num) -> new JSONNumber(num),
          (pojo0)->SerializationGWTUtilities.toListJson_FuncJ(
          pojo0,
          (pojo) -> SerializationGWTJson_Sample.fromPojoSimple(pojo))
          )
          );
    }
    if(structure.getNumberLong()!=null) {
      jsonObject.put("id",new JSONNumber(structure.getNumberLong()));
    }
    return jsonObject;
  }
}
