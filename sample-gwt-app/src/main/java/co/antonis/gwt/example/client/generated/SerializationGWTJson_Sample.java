package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import java.util.logging.Logger;

/**
 * Generated for total 2 structures 
 * 
 * @see co.antonis.generator.model.sample.PojoSimple [fields:4/4] 
 * @see co.antonis.generator.model.sample.PojoParent [fields:22/22] 
 */
public class SerializationGWTJson_Sample {
  public static Logger log = Logger.getLogger("");

  public static JSONObject fromPojoSimple(PojoSimple structure) {
    if (structure == null)  {
      return null;
    }
    JSONObject jsonObject = new JSONObject();
    if(structure.getDateSimple()!=null) {
      jsonObject.put("dateSimple",SerializationGWTUtilities.toVFromDate(structure.getDateSimple()));
    }
    if(structure.getListDateSimple()!=null) {
    }
    jsonObject.put("intSimple",new JSONNumber(structure.getIntSimple()));
    if(structure.getStringSimple()!=null) {
      jsonObject.put("stringSimple",new JSONString(structure.getStringSimple()));
    }
    return jsonObject;
  }

  public static JSONObject fromPojoParent(PojoParent structure) {
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
    jsonObject.put("isBooleanValue", JSONBoolean.getInstance(structure.isBooleanValue()));
    if(structure.getDate()!=null) {
      jsonObject.put("date",SerializationGWTUtilities.toVFromDate(structure.getDate()));
    }
    if(structure.getListString()!=null) {
      jsonObject.put("asdasd",SerializationGWTUtilities.toListJson_FuncJ(structure.getListString(),(s)->new JSONString(s)))
    }
    if(structure.getListDouble()!=null) {
    }
    if(structure.getListLong()!=null) {
    }
    if(structure.getListMapChild()!=null) {
    }
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
