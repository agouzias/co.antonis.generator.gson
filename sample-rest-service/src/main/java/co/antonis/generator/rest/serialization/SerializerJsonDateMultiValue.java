package co.antonis.generator.rest.serialization;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Double way to deserialize .
 * The correct approach is 2012-04-23T18:25:43.511Z - Always UTC
 * https://stackoverflow.com/questions/10286204/what-is-the-right-json-date-format/15952652#15952652
 */
public class SerializerJsonDateMultiValue implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private final DateFormat dateFormat_ISO8601;
    private final DateFormat dateFormatOriginal;

    //public static String FormatDate = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; /*2018-06-14T06:40:28.497Z*/
    //2020-05-22T20:46:54+00:00
    public static String FormatDate_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; /*2019-05-08T21:30:00.000Z JavaScript Friendly Warning ALWAYS UTC, used to parse dates from javascript*/
    public static String FormatDateOriginal = "MMM d, yyyy hh:mm:ss a"; /*May 9, 2018 11:57:00 PM Original*/

    public SerializerJsonDateMultiValue() {
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat_ISO8601 = new SimpleDateFormat(FormatDate_ISO8601, Locale.US);
        dateFormat_ISO8601.setTimeZone(TimeZone.getTimeZone("UTC"));

        dateFormatOriginal = new SimpleDateFormat(FormatDateOriginal, Locale.US);
        dateFormatOriginal.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String args[]) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new SerializerJsonDateMultiValue()).create();
        System.out.println(gson.toJson(new Date()));
        System.out.println(gson.toJson(new MDate()));
        System.out.println(gson.fromJson("{\"date\":\"May 22, 2020, 11:44:31 PM\"}", MDate.class));
        SimpleDateFormat dateFormat = new SimpleDateFormat(FormatDateOriginal, Locale.US);
        System.out.println(dateFormat.format(new Date()));
    }

    static class MDate {
        Date date = new Date();
    }

    //Default Serializer:
    //2018-06-14T14:32:24.548Z --> "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                         JsonDeserializationContext jsonDeserializationContext) {
        try {
            //Try with the default.
            return dateFormat_ISO8601.parse(jsonElement.getAsString());
        } catch (ParseException e1) {
            //Try with the original Date Format
            try {
                return dateFormatOriginal.parse(jsonElement.getAsString());
            } catch (ParseException e2) {
                //Try as Long
                try {
                    return new Date(jsonElement.getAsLong());
                } catch (Exception exc) {
                    //throw new JsonParseException(e2);
                    return new Date();
                }
            }
        }
    }

    public static Date parseOrNull(DateFormat df, String dateString) {
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }


    @Override
    public synchronized JsonElement serialize(Date date, Type type,
                                              JsonSerializationContext jsonSerializationContext) {
        synchronized (dateFormat_ISO8601) {
            String dateFormatAsString = dateFormat_ISO8601.format(date);
            return new JsonPrimitive(dateFormatAsString);
        }
    }

}
