package co.antonis.generator.model.sample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Sample class with various members to check serialization from/to json
 * in GWT
 */
public class PojoParent {
    @Expose
    Date date;

    @Expose
    String string;

    @Expose
    String stringNotSet;

    @Expose
    int numberInt;

    @Expose
    double numberDouble;

    @Expose
    boolean booleanValue;

    @Expose
    @SerializedName("id")
    Long numberLong;

    @Expose
    @SerializedName("idL")
    Long numberLongClass;

    @Expose
    @SerializedName("listString")
    List<String> listString;

    @Expose
    @SerializedName("listLong")
    List<Long> listLong;

    @Expose
    @SerializedName("listDouble")
    List<Double> listDouble;

    @Expose
    char charA;

    @Expose
    char charNotSet;

    @Expose
    List listAny;

    @Expose
    Map<Integer, String> mapString;

    @Expose
    @SerializedName("nameOfType")
    PojoType pojoType;

    @Expose
    @SerializedName("listPojoType")
    List<PojoType> listPojoType;

    @Expose
    @SerializedName("child")
    PojoSimple simple;

    @Expose
    @SerializedName("mapChild")
    Map<Integer, PojoSimple> mapChild;

    @Expose
    @SerializedName("mapListChild")
    Map<Integer, List<PojoSimple>> mapListChild;

    @Expose
    List<Map<Integer, List<PojoSimple>>> listMapChild;

    public static class PojoParentInner {
        @Expose
        @SerializedName("stringB")
        String stringB;

        public String getStringB() {
            return stringB;
        }

        public void setStringB(String stringB) {
            this.stringB = stringB;
        }
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public PojoType getPojoType() {
        return pojoType;
    }

    public void setPojoType(PojoType pojoType) {
        this.pojoType = pojoType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public long getNumberLong() {
        return numberLong;
    }

    public void setNumberLong(long numberLong) {
        this.numberLong = numberLong;
    }

    public Long getNumberLongClass() {
        return numberLongClass;
    }

    public void setNumberLongClass(Long numberLongClass) {
        this.numberLongClass = numberLongClass;
    }

    public List<String> getListString() {
        return listString;
    }

    public void setListString(List<String> listString) {
        this.listString = listString;
    }

    public Map<Integer, String> getMapString() {
        return mapString;
    }

    public void setMapString(Map<Integer, String> mapString) {
        this.mapString = mapString;
    }

    public void setNumberLong(Long numberLong) {
        this.numberLong = numberLong;
    }

    public List getListAny() {
        return listAny;
    }

    public void setListAny(List listAny) {
        this.listAny = listAny;
    }

    public List<Long> getListLong() {
        return listLong;
    }

    public void setListLong(List<Long> listLong) {
        this.listLong = listLong;
    }

    public List<Double> getListDouble() {
        return listDouble;
    }

    public void setListDouble(List<Double> listDouble) {
        this.listDouble = listDouble;
    }

    public char getCharA() {
        return charA;
    }

    public void setCharA(char charA) {
        this.charA = charA;
    }

    public String getStringNotSet() {
        return stringNotSet;
    }

    public void setStringNotSet(String stringNotSet) {
        this.stringNotSet = stringNotSet;
    }

    public int getNumberInt() {
        return numberInt;
    }

    public void setNumberInt(int numberInt) {
        this.numberInt = numberInt;
    }

    public double getNumberDouble() {
        return numberDouble;
    }

    public void setNumberDouble(double numberDouble) {
        this.numberDouble = numberDouble;
    }

    public char getCharNotSet() {
        return charNotSet;
    }

    public void setCharNotSet(char charNotSet) {
        this.charNotSet = charNotSet;
    }

    public PojoSimple getSimple() {
        return simple;
    }

    public void setSimple(PojoSimple simple) {
        this.simple = simple;
    }

    public Map<Integer, PojoSimple> getMapChild() {
        return mapChild;
    }

    public void setMapChild(Map<Integer, PojoSimple> mapChild) {
        this.mapChild = mapChild;
    }

    public Map<Integer, List<PojoSimple>> getMapListChild() {
        return mapListChild;
    }

    public void setMapListChild(Map<Integer, List<PojoSimple>> mapListChild) {
        this.mapListChild = mapListChild;
    }

    public List<PojoType> getListPojoType() {
        return listPojoType;
    }

    public void setListPojoType(List<PojoType> listPojoType) {
        this.listPojoType = listPojoType;
    }

    public List<Map<Integer, List<PojoSimple>>> getListMapChild() {
        return listMapChild;
    }

    public void setListMapChild(List<Map<Integer, List<PojoSimple>>> listMapChild) {
        this.listMapChild = listMapChild;
    }

    @Override
    public String toString() {
        String prefix = "\r\n";
        return "PojoParent{" +
               prefix + "date=" + date +
               prefix + ", string='" + string + '\'' +
               prefix + ", stringNotSet='" + stringNotSet + "\' is null:" + (stringNotSet == null) +
               prefix + ", numberInt=" + numberInt +
               prefix + ", numberDouble=" + numberDouble +
               prefix + ", booleanValue=" + booleanValue +
               prefix + ", numberLong=" + numberLong +
               prefix + ", numberLongClass=" + numberLongClass +
               prefix + ", listString=" + listString +
               prefix + ", listLong=" + listLong +
               prefix + ", listDouble=" + listDouble +
               prefix + ", charA=" + charA +
               prefix + ", charNotSet=" + charNotSet +
               prefix + ", listAny=" + listAny +
               prefix + ", mapString=" + StructureGenerator.toString(mapString) +
               prefix + ", pojoType=" + pojoType +
               prefix + ", pojoSimple=" + simple +
               prefix + ", listPojoType=" + StructureGenerator.toString(listPojoType) +
               prefix + ", mapChild=" + StructureGenerator.toString(mapChild) +
               '}';
    }
}
