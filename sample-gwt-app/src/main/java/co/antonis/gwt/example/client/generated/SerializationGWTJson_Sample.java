package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
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

  public static JsonObject fromPojoSimple(PojoSimple structure) {
    if (structure == null)  {
      return null;
    }
    JSONObject jsonObject = new JSONObject();
    log.info("Field:dateSimple:"+structure.getDateSimple());
    if(structure.getDateSimple()!=null) {
      jsonObject.put("dateSimple",SerializationGWTUtilities.toVFromDate(structure.getDateSimple()));
    }
    log.info("Field:listDateSimple:"+structure.getListDateSimple());
    if(structure.getListDateSimple()!=null) {
    }
    log.info("Field:intSimple:"+structure.getIntSimple());
    jsonObject.put("intSimple",new JSONNumber(structure.getIntSimple()));
    log.info("Field:stringSimple:"+structure.getStringSimple());
    if(structure.getStringSimple()!=null) {
      jsonObject.put("stringSimple",new JSONString(structure.getStringSimple()));
    }
    return jsonObject;
  }

  public static JsonObject fromPojoParent(PojoParent structure) {
    if (structure == null)  {
      return null;
    }
    JSONObject jsonObject = new JSONObject();
    log.info("Field:string:"+structure.getString());
    if(structure.getString()!=null) {
      jsonObject.put("string",new JSONString(structure.getString()));
    }
    log.info("Field:mapString:"+structure.getMapString());
    if(structure.getMapString()!=null) {
    }
    log.info("Field:stringNotSet:"+structure.getStringNotSet());
    if(structure.getStringNotSet()!=null) {
      jsonObject.put("stringNotSet",new JSONString(structure.getStringNotSet()));
    }
    log.info("Field:pojoType:"+structure.getPojoType());
    if(structure.getPojoType()!=null) {
      jsonObject.put("nameOfType",new JSONString(structure.getPojoType().name()));
    }
    log.info("Field:isBooleanValue:"+structure.isBooleanValue());
    jsonObject.put("isBooleanValue", JSONBoolean.getInstance(structure.isBooleanValue()));
    log.info("Field:date:"+structure.getDate());
    if(structure.getDate()!=null) {
      jsonObject.put("date",SerializationGWTUtilities.toVFromDate(structure.getDate()));
    }
    log.info("Field:listString:"+structure.getListString());
    if(structure.getListString()!=null) {
    }
    log.info("Field:listDouble:"+structure.getListDouble());
    if(structure.getListDouble()!=null) {
    }
    log.info("Field:listLong:"+structure.getListLong());
    if(structure.getListLong()!=null) {
    }
    log.info("Field:listMapChild:"+structure.getListMapChild());
    if(structure.getListMapChild()!=null) {
    }
    log.info("Field:listPojoType:"+structure.getListPojoType());
    if(structure.getListPojoType()!=null) {
    }
    log.info("Field:simple:"+structure.getSimple());
    if(structure.getSimple()!=null) {
      jsonObject.put("child",SerializationGWTJson_Sample.fromPojoSimple(structure.getSimple().isObject().toString()));
    }
    log.info("Field:charA:"+structure.getCharA());
    // Not implemented. Consider to add it manually or ignore field
    log.info("Field:numberLongClass:"+structure.getNumberLongClass());
    if(structure.getNumberLongClass()!=null) {
      jsonObject.put("idL",new JSONNumber(structure.getNumberLongClass()));
    }
    log.info("Field:listAny:"+structure.getListAny());
    if(structure.getListAny()!=null) {
    }
    log.info("Field:numberInt:"+structure.getNumberInt());
    jsonObject.put("numberInt",new JSONNumber(structure.getNumberInt()));
    log.info("Field:numberDouble:"+structure.getNumberDouble());
    jsonObject.put("numberDouble",new JSONNumber(structure.getNumberDouble()));
    log.info("Field:charNotSet:"+structure.getCharNotSet());
    // Not implemented. Consider to add it manually or ignore field
    log.info("Field:mapChild:"+structure.getMapChild());
    if(structure.getMapChild()!=null) {
    }
    log.info("Field:boolValue:"+structure.isBoolValue());
    jsonObject.put("boolValue",JSONBoolean.getInstance(structure.isBoolValue()));
    log.info("Field:mapListChild:"+structure.getMapListChild());
    if(structure.getMapListChild()!=null) {
    }
    log.info("Field:numberLong:"+structure.getNumberLong());
    if(structure.getNumberLong()!=null) {
      jsonObject.put("id",new JSONNumber(structure.getNumberLong()));
    }
    return jsonObject;
  }
}
