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
    }
    if(structure.getListDouble()!=null) {
    }
    if(structure.getListLong()!=null) {
    }
    if(structure.getListMapChild()!=null) {
    }
    jsonObject.put("numberB",new JSONNumber(structure.getNumberB()));
    if(structure.getListPojoType()!=null) {
    }
    if(structure.getSimple()!=null) {
      jsonObject.put("child",SerializationGWTJson_Sample.fromPojoSimple(structure.getSimple()));
    }
    // Not implemented. Consider to add it manually or ignore field
    if(structure.getNumberLongClass()!=null) {
      jsonObject.put("idL",new JSONNumber(structure.getNumberLongClass()));
    }
    if(structure.getListAny()!=null) {
    }
    jsonObject.put("numberInt",new JSONNumber(structure.getNumberInt()));
    jsonObject.put("numberDouble",new JSONNumber(structure.getNumberDouble()));
    // Not implemented. Consider to add it manually or ignore field
    if(structure.getMapChild()!=null) {
    }
    jsonObject.put("boolValue",JSONBoolean.getInstance(structure.isBoolValue()));
    if(structure.getMapListChild()!=null) {
    }
    if(structure.getNumberLong()!=null) {
      jsonObject.put("id",new JSONNumber(structure.getNumberLong()));
    }
    return jsonObject;
  }
}
