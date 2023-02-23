package co.antonis.generator.model.samplePojo;

import co.antonis.generator.model.samplePojo.sub.PojoChild;

import java.util.*;

public class StructureGenerator {

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
        return structure;
    }

    public static PojoSimple generatePojoSimple() {
        PojoSimple structure = new PojoSimple();
        structure.setDateSimple(new Date());
        structure.setIntSimple(11);
        structure.setStringSimple("antonis");
        structure.setListDateSimple(Arrays.asList(new Date(), new Date(), new Date()));
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
        return toString(map, "\r\n");
    }

    public static String toString(Map<?, ?> map, String separator) {
        if (map == null)
            return "n/a";
        StringBuilder strB = new StringBuilder();
        for (Object key : map.keySet()) {
            strB.append("{").append(key).append("}");
            strB.append("{").append(map.get(key)).append("}");
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
}
