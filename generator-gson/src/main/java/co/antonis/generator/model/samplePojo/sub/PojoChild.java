package co.antonis.generator.model.samplePojo.sub;

import co.antonis.generator.model.samplePojo.PojoParent;
import com.google.gson.annotations.Expose;

public class PojoChild extends PojoParent {
    @Expose
    int numberB;

    public int getNumberB() {
        return numberB;
    }

    public void setNumberB(int numberB) {
        this.numberB = numberB;
    }
}
