package co.antonis.generator.model.sample;

import co.antonis.generator.model.sample.sub.PojoChild;

import java.util.*;
import java.util.function.Function;

public class StructureGenerator {

    public static long HOUR_MS = 60 * 60 * 1000;
    public static long DAY_MS = 24 * HOUR_MS;

    public static PojoParent generatePojoParent() {
        PojoParent structure = new PojoParent();
        structure.setListString(Arrays.asList("a", "b", "c", null, "d"));
        structure.setDate(new Date());
        structure.setNumberInt(1578);
        structure.setNumberDouble(1.578);
        structure.setCharA('a');
        structure.setPojoType(PojoType.A);
        structure.setString("this is a \"string\"... using quotes for test");
        structure.setBooleanValue(true);
        structure.setNumberLongClass(567L);
        structure.setListAny(Arrays.asList("a", 11, "b", 12));
        structure.setListDouble(Arrays.asList(0.1d, 0.2, null, 0.3));
        structure.setMapString(toMap(new Integer[]{1, 2, 3, 4}, new String[]{"a", "b", null, "d"}));
        structure.setSimple(generatePojoSimple());
        structure.setMapChild(toMap(new Integer[]{11, 12}, new PojoSimple[]{null, generatePojoSimple()}));

        Map<Integer, List<PojoSimple>> mapListChild = new HashMap<>();
        mapListChild.put(1, Arrays.<PojoSimple>asList(generatePojoSimple(), generatePojoSimple()));
        mapListChild.put(2, Arrays.<PojoSimple>asList(generatePojoSimple(), generatePojoSimple()));
        structure.setMapListChild(mapListChild);


        return structure;
    }

    public static PojoSimple generatePojoSimple() {
        PojoSimple structure = new PojoSimple();
        structure.setDateSimple(new Date());
        structure.setIntSimple(11);
        structure.setStringSimple("antonis");
        structure.setListDateSimple(Arrays.asList(daysBefore( (int)Math.round(Math.random()*20)), daysBefore(2), daysBefore(3)));
        return structure;
    }

    public static PojoParent generateSub() {
        PojoChild structure = new PojoChild();
        structure.setListString(Arrays.asList("a", "b", "c", null, "d"));
        structure.setDate(new Date());
        structure.setNumberInt(1578);
        structure.setNumberDouble(1.578);
        structure.setCharA('a');
        structure.setString("this is a \"string\"... using quotes for test");
        structure.setBooleanValue(true);
        structure.setNumberLongClass(567L);
        structure.setListAny(Arrays.asList("a", 11, "b", 12));
        structure.setListDouble(Arrays.asList(0.1d, 0.2, null, 0.3));
        structure.setMapString(toMap(new Integer[]{1, 2, 3, 4}, new String[]{"a", "b", null, "d"}));

        structure.setNumberB(11111);
        return structure;
    }

    public static String toString(List list) {
        return toString(list, ",", true);
    }

    public static String toString(List list, String separator, boolean inBrackets) {
        if (list == null)
            return "n/a";
        StringBuilder strB = new StringBuilder();
        if (inBrackets)
            for (Object item : list) {
                if (item != null)
                    strB.append("{").append(item.toString()).append("}").append(separator);
            }
        else
            for (Object item : list) {
                if (item != null)
                    strB.append(item.toString()).append(separator);
            }
        return strB.toString();
    }

    public static String toString(Map map) {
        return toString(map, Object::toString, "\r\n");
    }

    public static <T> String toString(Map<?, T> map, Function<T, String> printer, String separator) {
        if (map == null)
            return "n/a";
        StringBuilder strB = new StringBuilder();
        for (Object key : map.keySet()) {
            strB.append("{").append(key).append("}");
            strB.append("{").append(printer.apply(map.get(key))).append("}");
            strB.append(separator);
        }
        return strB.toString();
    }

    public static <K, V> Map<K, V> toMap(K[] k, V[] v) {
        HashMap map = new HashMap();
        for (int i = 0; i < k.length; i++)
            map.put(k[i], v[i]);
        return map;
    }

    public static Date daysBefore(int days) {
        return new Date(System.currentTimeMillis() - (days * DAY_MS));
    }
}
