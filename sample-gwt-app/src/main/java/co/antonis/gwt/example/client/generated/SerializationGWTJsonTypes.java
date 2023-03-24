package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import co.antonis.generator.model.sample.sub.PojoChild;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;

public class SerializationGWTJsonTypes {
  public static Class[] classes = new Class<?>[] {
  PojoSimple.class,
  PojoParent.class,
  PojoChild.class,
  };

  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoSimple.class) {
      return (T)SerializationGWTJson_Sample.toPojoSimple(json);
    }
    if(clazz==PojoParent.class) {
      return (T)SerializationGWTJson_Sample.toPojoParent(json);
    }
    if(clazz==PojoChild.class) {
      return (T)SerializationGWTJson_Sub.toPojoChild(json);
    }
    return null;
  }

  public static String toJson(Object structure) {
    if(structure == null) {
      return null;
    }
    if(structure instanceof PojoSimple) {
      return SerializationGWTJson_Sample.fromPojoSimple((PojoSimple)structure).toString();
    }
    if(structure instanceof PojoChild) {
      return SerializationGWTJson_Sub.fromPojoChild((PojoChild)structure).toString();
    }
    if(structure instanceof PojoParent) {
      return SerializationGWTJson_Sample.fromPojoParent((PojoParent)structure).toString();
    }
    return null;
  }
}
