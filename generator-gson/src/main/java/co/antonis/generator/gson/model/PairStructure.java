package co.antonis.generator.gson.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Utility Class that wraps two structures.
 *
 * @param <F>
 * @param <S>
 */
public class PairStructure<F, S> implements Serializable, Comparable<PairStructure<F, S>> {

    @Expose
    @SerializedName("a")
    protected F first;

    @Expose
    @SerializedName("b")
    protected S second;

    public PairStructure() {
    }

    public static <F,S> PairStructure<F,S> n(F a, S b){
        return new PairStructure<>(a,b);
    }

    public PairStructure(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public PairStructure<F,S> setFirst(F first) {
        this.first = first;
        return this;
    }

    public S getSecond() {
        return second;
    }

    public PairStructure<F,S> setSecond(S second) {
        this.second = second;
        return this;
    }

    public boolean isNotNull(){
        return first!=null && second!=null;
    }

    @Override
    public int compareTo(PairStructure<F, S> o) {
        if (first instanceof Comparable) {
            return ((Comparable) first).compareTo(o.getFirst());
        }
        return 0;
    }
}
