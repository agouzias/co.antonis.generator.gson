package co.antonis.gwt.example.client.generated;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.PojoSimple;
import co.antonis.generator.model.sample.sub.PojoChild;
import java.lang.Class;
import java.lang.Object;
import java.lang.String;

public class SerGwtJsonTypes {
  public static Class[] classes = new Class<?>[] {
  PojoParent.class,
  PojoChild.class,
  PojoSimple.class,
  };

  public static <T> T fromJson(Class<T> clazz, String json) {
    if(clazz==PojoParent.class) {
      return (T)SerGwtJson_Sample.toPojoParent(json);
    }
    if(clazz==PojoChild.class) {
      return (T)SerGwtJson_Sub.toPojoChild(json);
    }
    if(clazz==PojoSimple.class) {
      return (T)SerGwtJson_Sample.toPojoSimple(json);
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
    if(structure instanceof PojoChild) {
      return SerGwtJson_Sub.fromPojoChild((PojoChild)structure).toString();
    }
    if(structure instanceof PojoSimple) {
      return SerGwtJson_Sample.fromPojoSimple((PojoSimple)structure).toString();
    }
    return null;
  }
}
