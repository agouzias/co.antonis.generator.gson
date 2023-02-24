package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.samplePojo.PojoParent;
import java.lang.Class;
import java.lang.String;

public class SerializationGWTJsonTypes {
  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoParent.class) {
      return (T)SerializationGWTJson.toPojoParent(json);
    }
    return null;
  }
}
