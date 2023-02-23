package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.samplePojo.PojoParent;
import co.antonis.generator.model.samplePojo.PojoSimple;
import co.antonis.generator.model.samplePojo.sub.PojoChild;
import java.lang.Class;
import java.lang.String;

public class SerializationGWTJsonTypes {
  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoChild.class) {
      return (T)SerializationGWTJson.toPojoChild(json);
    }
    if(clazz==PojoSimple.class) {
      return (T)SerializationGWTJson.toPojoSimple(json);
    }
    if(clazz==PojoParent.class) {
      return (T)SerializationGWTJson.toPojoParent(json);
    }
    return null;
  }
}
