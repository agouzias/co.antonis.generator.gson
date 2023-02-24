package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.samplePojo.PojoParent;
import co.antonis.generator.model.samplePojo.PojoType;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import java.lang.String;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Generated for total 1 structures 
 * 
 * co.antonis.generator.model.samplePojo.PojoParent [fields:20/20] 
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

  public static PojoParent toPojoParent(String json) {
    JSONValue jsonValue = JSONParser.parseStrict(json);
    JSONObject jsonObject = jsonValue.isObject();
    
    if (jsonObject != null)  {
      PojoParent structure = new PojoParent();
      if(jsonObject.get("id")!=null) {
        structure.setNumberLong(Long.parseLong(jsonObject.get("id").toString()));
      }
      if(jsonObject.get("booleanValue")!=null) {
        structure.setBooleanValue(Boolean.parseBoolean(jsonObject.get("booleanValue").toString()));
      }
      if(jsonObject.get("mapListChild")!=null) {
        structure.setMapListChild(
            SerializationGWTUtilities.toMap_FromS(
            jsonObject.get("mapListChild"),
            (s)->s!=null ? Integer.parseInt(s) : null,
            null /*TODO Implement the data*/)
            );
      }
      if(jsonObject.get("mapChild")!=null) {
        structure.setMapChild(
            SerializationGWTUtilities.toMap_FromS(
            jsonObject.get("mapChild"),
            (s)->s!=null ? Integer.parseInt(s) : null,
            null)
            );
      }
      if(jsonObject.get("charNotSet")!=null) {
        structure.setCharNotSet(jsonObject.get("charNotSet").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("numberInt")!=null) {
        structure.setNumberInt(Integer.parseInt(jsonObject.get("numberInt").toString()));
      }
      if(jsonObject.get("numberDouble")!=null) {
        structure.setNumberDouble(Double.parseDouble(jsonObject.get("numberDouble").toString()));
      }
      if(jsonObject.get("idL")!=null) {
        structure.setNumberLongClass(Long.parseLong(jsonObject.get("idL").toString()));
      }
      // TODO [listAny][interface java.util.List] NOT serialized Not type parameters in container
      if(jsonObject.get("charA")!=null) {
        structure.setCharA(jsonObject.get("charA").isString().stringValue().charAt(0));
      }
      if(jsonObject.get("listPojoType")!=null) {
        structure.setListPojoType(
            SerializationGWTUtilities.toList_FromS(
            jsonObject.get("listPojoType"),
            (s) -> co.antonis.generator.model.samplePojo.PojoType.valueOf(s))
            );
      }
      // TODO [simple][class co.antonis.generator.model.samplePojo.PojoSimple] NOT serialized Warning, Not supported type, please check
      if(jsonObject.get("listLong")!=null) {
        structure.setListLong(
            SerializationGWTUtilities.toList_FromS(
            jsonObject.get("listLong"),
            (s)-> s!=null ?Long.parseLong(s) : null)
            );
      }
      if(jsonObject.get("listDouble")!=null) {
        structure.setListDouble(
            SerializationGWTUtilities.toList_FromS(
            jsonObject.get("listDouble"),
            (s)-> s!=null ? Double.parseDouble(s) : null)
            );
      }
      if(jsonObject.get("listString")!=null) {
        structure.setListString(
            SerializationGWTUtilities.toList_FromS(
            jsonObject.get("listString"),
            (s)->s)
            );
      }
      if(jsonObject.get("date")!=null) {
        structure.setDate(toDateFromV(jsonObject.get("date")/*.isString().stringValue()*/));
      }
      if(jsonObject.get("stringNotSet")!=null) {
        structure.setStringNotSet(jsonObject.get("stringNotSet").isString().stringValue());
      }
      if(jsonObject.get("nameOfType")!=null) {
        structure.setPojoType(PojoType.valueOf(jsonObject.get("nameOfType").isString().stringValue()));
      }
      if(jsonObject.get("string")!=null) {
        structure.setString(jsonObject.get("string").isString().stringValue());
      }
      if(jsonObject.get("mapString")!=null) {
        structure.setMapString(
            SerializationGWTUtilities.toMap_FromS(
            jsonObject.get("mapString"),
            (s)->s!=null ? Integer.parseInt(s) : null,
            (s)->s)
            );
      }
      return structure;
    }
    return null;
  }
}
