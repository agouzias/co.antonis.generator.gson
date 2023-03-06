package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import co.antonis.generator.model.sample.sub.PojoChild;
import java.lang.Class;
import java.lang.String;

public class SerializationGWTJsonTypes {
  public static Class[] classes = new Class<?>[] {
  PojoParent.class,
  PojoChild.class,
  PojoSimple.class,
  };


  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoParent.class) {
      return (T)SerializationGWTJson_Sample.toPojoParent(json);
    }
    if(clazz==PojoChild.class) {
      return (T)SerializationGWTJson_Sub.toPojoChild(json);
    }
    if(clazz==PojoSimple.class) {
      return (T)SerializationGWTJson_Sample.toPojoSimple(json);
    }
    return null;
  }
}
