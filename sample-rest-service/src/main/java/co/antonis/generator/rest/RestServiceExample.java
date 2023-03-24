package co.antonis.generator.rest;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.generator.model.sample.StructureGenerator;
import co.antonis.generator.rest.serialization.SerializerJsonDateMultiValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class RestServiceExample {
    Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Date.class, new SerializerJsonDateMultiValue()).create();

    public RestServiceExample() {
        System.out.println("test");
    }

    @GetMapping("/jsonTo")
    public String toJson(@RequestParam(value = "type", defaultValue = "0") Integer type) {
        if (type == null || type == 0)
            return gson.toJson(StructureGenerator.generatePojoParent());
        else if (type == 1)
            return gson.toJson(StructureGenerator.generateSub());
        return gson.toJson(StructureGenerator.generatePojoParent());
    }

    @GetMapping("/jsonFrom")
    public String fromJson(@RequestParam(value = "param", defaultValue = "0") String json) {
        PojoParent parentFromJson = gson.fromJson(json, PojoParent.class);
        PojoParent parentOriginal = StructureGenerator.generatePojoParent();
        System.out.println(parentOriginal.equalsTest(parentFromJson));
        return "ok";
    }
}
