package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.samplePojo.PojoParent;
import co.antonis.generator.model.samplePojo.PojoSimple;
import co.antonis.generator.model.samplePojo.PojoType;
import co.antonis.generator.model.samplePojo.sub.PojoChild;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import java.lang.String;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Generated for total 3 structures 
 * 
 * co.antonis.generator.model.samplePojo.PojoSimple [fields:4/4] 
 * co.antonis.generator.model.samplePojo.PojoParent [fields:18/18] 
 * co.antonis.generator.model.samplePojo.sub.PojoChild [fields:19/1] 
 */
public class SerializationGWTJson {
  public static Logger log = Logger.getLogger("");

  public static DateTimeFormat dateFormat_ISO8601 = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  public static DateTimeFormat dateFormat_Original = DateTimeFormat.getFormat("MMM, d, yyyy hh:mm:ss a");

  public static Date toDateFromV(JSONValue jsonDate) {
    if (jsonDate == null)
        return null;
    if (jsonDate.isNumber() != null)
        try {
            return new java.util.Date((long) jsonDate.isNumber().doubleValue());
        } catch (Exception ignored) {
        }
    if (jsonDate.isString() != null) {
        try {
            return dateFormat_ISO8601.parse(jsonDate.isString().stringValue());
        } catch (Exception ignored) {
        }
        return dateFormat_Original.parse(jsonDate.isString().stringValue());
    }
    throw new RuntimeException("Unable to convert date from json "+jsonDate);
  }

  public static Date toDateFromS(String jsonDate) {
    if(jsonDate == null)
        return null;
     //the toString() add quotes
     jsonDate = jsonDate.replaceAll("\"","");
    try {
        return new java.util.Date(Long.parseLong(jsonDate));
    } catch (Exception ignored) {}
    try {
        return dateFormat_ISO8601.parse(jsonDate);
    } catch (Exception ignored){}
    return dateFormat_Original.parse(jsonDate);
  }

  public static PojoSimple toPojoSimple(String json) {
    JSONValue jsonValue = JSONParser.parseStrict(json);
    JSONObject jsonObject = jsonValue.isObject();
    
    if (jsonObject != null)  {
      PojoSimple structure = new PojoSimple();
      if(jsonObject.get("stringSimple")!=null) {
        log.info("Field:stringSimple:"+jsonObject.get("stringSimple"));
        structure.setStringSimple(jsonObject.get("stringSimple").isString().stringValue());
      }
      if(jsonObject.get("intSimple")!=null) {
        log.info("Field:intSimple:"+jsonObject.get("intSimple"));
        structure.setIntSimple(Integer.parseInt(jsonObject.get("intSimple").toString()));
      }
      if(jsonObject.get("dateSimple")!=null) {
        log.info("Field:dateSimple:"+jsonObject.get("dateSimple"));
        structure.setDateSimple(toDateFromV(jsonObject.get("dateSimple")/*.isString().stringValue()*/));
      }
      if(jsonObject.get("listDateSimple")!=null) {
        log.info("Field:listDateSimple:"+jsonObject.get("listDateSimple"));
        structure.setListDateSimple(SerializationGWTUtilities.toListFromS(jsonObject.get("listDateSimple"),(s)->toDateFromS(s)));
      }
      return structure;
    }
    return null;
  }

  public static String fromPojoSimple(PojoSimple structure) {
    return "TODO";
  }

  public static PojoParent toPojoParent(String json) {
    JSONValue jsonValue = JSONParser.parseStrict(json);
    JSONObject jsonObject = jsonValue.isObject();
    
    if (jsonObject != null)  {
      PojoParent structure = new PojoParent();
      if(jsonObject.get("id")!=null) {
        log.info("Field:numberLong:"+jsonObject.get("id"));
        structure.setNumberLong(Long.parseLong(jsonObject.get("id").toString()));
      }
      if(jsonObject.get("booleanValue")!=null) {
        log.info("Field:booleanValue:"+jsonObject.get("booleanValue"));
        structure.setBooleanValue(Boolean.parseBoolean(jsonObject.get("booleanValue").toString()));
      }
      if(jsonObject.get("mapChild")!=null) {
        log.info("Field:mapChild:"+jsonObject.get("mapChild"));
        structure.setMapChild(SerializationGWTUtilities.toMapFromS(jsonObject.get("mapChild"),(s)->s!=null ? Integer.parseInt(s) : null,(s) -> toPojoSimple(s)));
      }
      if(jsonObject.get("charNotSet")!=null) {
        log.info("Field:charNotSet:"+jsonObject.get("charNotSet"));
        structure.setCharNotSet(jsonObject.get("charNotSet").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("numberInt")!=null) {
        log.info("Field:numberInt:"+jsonObject.get("numberInt"));
        structure.setNumberInt(Integer.parseInt(jsonObject.get("numberInt").toString()));
      }
      if(jsonObject.get("numberDouble")!=null) {
        log.info("Field:numberDouble:"+jsonObject.get("numberDouble"));
        structure.setNumberDouble(Double.parseDouble(jsonObject.get("numberDouble").toString()));
      }
      if(jsonObject.get("idL")!=null) {
        log.info("Field:numberLongClass:"+jsonObject.get("idL"));
        structure.setNumberLongClass(Long.parseLong(jsonObject.get("idL").toString()));
      }
      log.info("Field:listAny:"+jsonObject.get("listAny"));
      //TODO Warning, [listAny][interface java.util.List] NOT serialized Not supported container;
      if(jsonObject.get("charA")!=null) {
        log.info("Field:charA:"+jsonObject.get("charA"));
        structure.setCharA(jsonObject.get("charA").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("child")!=null) {
        log.info("Field:simple:"+jsonObject.get("child"));
        structure.setSimple(toPojoSimple(jsonObject.get("child").isObject().toString()));
      }
      if(jsonObject.get("listLong")!=null) {
        log.info("Field:listLong:"+jsonObject.get("listLong"));
        structure.setListLong(SerializationGWTUtilities.toListFromS(jsonObject.get("listLong"),(s)-> s!=null ?Long.parseLong(s) : null));
      }
      if(jsonObject.get("listDouble")!=null) {
        log.info("Field:listDouble:"+jsonObject.get("listDouble"));
        structure.setListDouble(SerializationGWTUtilities.toListFromS(jsonObject.get("listDouble"),(s)-> s!=null ? Double.parseDouble(s) : null));
      }
      if(jsonObject.get("listString")!=null) {
        log.info("Field:listString:"+jsonObject.get("listString"));
        structure.setListString(SerializationGWTUtilities.toListFromS(jsonObject.get("listString"),(s)->s));
      }
      if(jsonObject.get("date")!=null) {
        log.info("Field:date:"+jsonObject.get("date"));
        structure.setDate(toDateFromV(jsonObject.get("date")/*.isString().stringValue()*/));
      }
      if(jsonObject.get("stringNotSet")!=null) {
        log.info("Field:stringNotSet:"+jsonObject.get("stringNotSet"));
        structure.setStringNotSet(jsonObject.get("stringNotSet").isString().stringValue());
      }
      if(jsonObject.get("nameOfType")!=null) {
        log.info("Field:pojoType:"+jsonObject.get("nameOfType"));
        structure.setPojoType(PojoType.valueOf(jsonObject.get("nameOfType").isString().stringValue()));
      }
      if(jsonObject.get("string")!=null) {
        log.info("Field:string:"+jsonObject.get("string"));
        structure.setString(jsonObject.get("string").isString().stringValue());
      }
      if(jsonObject.get("mapString")!=null) {
        log.info("Field:mapString:"+jsonObject.get("mapString"));
        structure.setMapString(SerializationGWTUtilities.toMapFromS(jsonObject.get("mapString"),(s)->s!=null ? Integer.parseInt(s) : null,(s)->s));
      }
      return structure;
    }
    return null;
  }

  public static String fromPojoParent(PojoParent structure) {
    return "TODO";
  }

  public static PojoChild toPojoChild(String json) {
    JSONValue jsonValue = JSONParser.parseStrict(json);
    JSONObject jsonObject = jsonValue.isObject();
    
    if (jsonObject != null)  {
      PojoChild structure = new PojoChild();
      if(jsonObject.get("id")!=null) {
        log.info("Field:numberLong:"+jsonObject.get("id"));
        structure.setNumberLong(Long.parseLong(jsonObject.get("id").toString()));
      }
      if(jsonObject.get("booleanValue")!=null) {
        log.info("Field:booleanValue:"+jsonObject.get("booleanValue"));
        structure.setBooleanValue(Boolean.parseBoolean(jsonObject.get("booleanValue").toString()));
      }
      if(jsonObject.get("mapChild")!=null) {
        log.info("Field:mapChild:"+jsonObject.get("mapChild"));
        structure.setMapChild(SerializationGWTUtilities.toMapFromS(jsonObject.get("mapChild"),(s)->s!=null ? Integer.parseInt(s) : null,(s) -> toPojoSimple(s)));
      }
      if(jsonObject.get("charNotSet")!=null) {
        log.info("Field:charNotSet:"+jsonObject.get("charNotSet"));
        structure.setCharNotSet(jsonObject.get("charNotSet").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("numberInt")!=null) {
        log.info("Field:numberInt:"+jsonObject.get("numberInt"));
        structure.setNumberInt(Integer.parseInt(jsonObject.get("numberInt").toString()));
      }
      if(jsonObject.get("numberDouble")!=null) {
        log.info("Field:numberDouble:"+jsonObject.get("numberDouble"));
        structure.setNumberDouble(Double.parseDouble(jsonObject.get("numberDouble").toString()));
      }
      if(jsonObject.get("idL")!=null) {
        log.info("Field:numberLongClass:"+jsonObject.get("idL"));
        structure.setNumberLongClass(Long.parseLong(jsonObject.get("idL").toString()));
      }
      log.info("Field:listAny:"+jsonObject.get("listAny"));
      //TODO Warning, [listAny][interface java.util.List] NOT serialized Not supported container;
      if(jsonObject.get("numberB")!=null) {
        log.info("Field:numberB:"+jsonObject.get("numberB"));
        structure.setNumberB(Integer.parseInt(jsonObject.get("numberB").toString()));
      }
      if(jsonObject.get("charA")!=null) {
        log.info("Field:charA:"+jsonObject.get("charA"));
        structure.setCharA(jsonObject.get("charA").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("child")!=null) {
        log.info("Field:simple:"+jsonObject.get("child"));
        structure.setSimple(toPojoSimple(jsonObject.get("child").isObject().toString()));
      }
      if(jsonObject.get("listLong")!=null) {
        log.info("Field:listLong:"+jsonObject.get("listLong"));
        structure.setListLong(SerializationGWTUtilities.toListFromS(jsonObject.get("listLong"),(s)-> s!=null ?Long.parseLong(s) : null));
      }
      if(jsonObject.get("listDouble")!=null) {
        log.info("Field:listDouble:"+jsonObject.get("listDouble"));
        structure.setListDouble(SerializationGWTUtilities.toListFromS(jsonObject.get("listDouble"),(s)-> s!=null ? Double.parseDouble(s) : null));
      }
      if(jsonObject.get("listString")!=null) {
        log.info("Field:listString:"+jsonObject.get("listString"));
        structure.setListString(SerializationGWTUtilities.toListFromS(jsonObject.get("listString"),(s)->s));
      }
      if(jsonObject.get("date")!=null) {
        log.info("Field:date:"+jsonObject.get("date"));
        structure.setDate(toDateFromV(jsonObject.get("date")/*.isString().stringValue()*/));
      }
      if(jsonObject.get("stringNotSet")!=null) {
        log.info("Field:stringNotSet:"+jsonObject.get("stringNotSet"));
        structure.setStringNotSet(jsonObject.get("stringNotSet").isString().stringValue());
      }
      if(jsonObject.get("nameOfType")!=null) {
        log.info("Field:pojoType:"+jsonObject.get("nameOfType"));
        structure.setPojoType(PojoType.valueOf(jsonObject.get("nameOfType").isString().stringValue()));
      }
      if(jsonObject.get("string")!=null) {
        log.info("Field:string:"+jsonObject.get("string"));
        structure.setString(jsonObject.get("string").isString().stringValue());
      }
      if(jsonObject.get("mapString")!=null) {
        log.info("Field:mapString:"+jsonObject.get("mapString"));
        structure.setMapString(SerializationGWTUtilities.toMapFromS(jsonObject.get("mapString"),(s)->s!=null ? Integer.parseInt(s) : null,(s)->s));
      }
      return structure;
    }
    return null;
  }

  public static String fromPojoChild(PojoChild structure) {
    return "TODO";
  }
}
