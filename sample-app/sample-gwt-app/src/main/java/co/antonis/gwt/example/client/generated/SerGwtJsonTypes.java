package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;

public class SerGwtJsonTypes {
  public static Class[] classes = new Class<?>[] {
  PojoParent.class,
  };

  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoParent.class) {
      return (T)SerGwtJson_Sample.toPojoParent(json);
    }
    return null;
  }

  public static String toJson(Object structure) {
    if(structure == null) {
      return null;
    }
    if(structure instanceof PojoParent) {
      return SerGwtJson_Sample.fromPojoParent((PojoParent)structure).toString();
    }
    return null;
  }
}
