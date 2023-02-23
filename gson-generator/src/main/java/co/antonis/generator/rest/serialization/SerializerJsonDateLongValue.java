package co.antonis.generator.rest.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Double way to deserialize.
 * Not Used, just a sample of Long/Parser
 */
public class SerializerJsonDateLongValue implements JsonDeserializer<Date>, JsonSerializer<Date> {


    public SerializerJsonDateLongValue() {
    }

    public static void main(String args[]) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,new SerializerJsonDateLongValue()).create();
        System.out.println(gson.toJson(new Date()));
        System.out.println(gson.toJson(new SerializerJsonDateMultiValue.MDate()));
        System.out.println(gson.fromJson("{\"date\":\"May 22, 2020, 11:44:31 PM\"}", SerializerJsonDateMultiValue.MDate.class));
    }

    //Default Serializer:
    //2018-06-14T14:32:24.548Z --> "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                         JsonDeserializationContext jsonDeserializationContext) {
        try {
            return new Date(jsonElement.getAsLong());
        } catch (Exception exc) {
            //throw new JsonParseException(e2);
            return new Date();
        }
    }


    @Override
    public synchronized JsonElement serialize(Date date, Type type,
                                              JsonSerializationContext jsonSerializationContext) {
        if(date!=null)
            return new JsonPrimitive(date.getTime());
        return JsonNull.INSTANCE;
    }

}
