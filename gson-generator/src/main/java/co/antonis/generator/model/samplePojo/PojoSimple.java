package co.antonis.generator.model.samplePojo;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

public class PojoSimple {
    @Expose
    String stringSimple;
    @Expose
    int intSimple;
    @Expose
    Date dateSimple;
    @Expose
    List<Date> listDateSimple;

    public String getStringSimple() {
        return stringSimple;
    }

    public void setStringSimple(String stringSimple) {
        this.stringSimple = stringSimple;
    }

    public int getIntSimple() {
        return intSimple;
    }

    public void setIntSimple(int intSimple) {
        this.intSimple = intSimple;
    }

    public Date getDateSimple() {
        return dateSimple;
    }

    public void setDateSimple(Date dateSimple) {
        this.dateSimple = dateSimple;
    }

    public List<Date> getListDateSimple() {
        return listDateSimple;
    }

    public void setListDateSimple(List<Date> listDateSimple) {
        this.listDateSimple = listDateSimple;
    }

    @Override
    public String toString() {
        String prefix="\r\n";
        return "PojoSimple{" +
                prefix + "stringSimple='" + stringSimple + '\'' +
                prefix + ", intSimple=" + intSimple +
                prefix + ", dateSimple=" + dateSimple +
                prefix + ", listDateSimple=" + listDateSimple +
                prefix + '}';
    }
}
