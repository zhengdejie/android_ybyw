package appframe.appframe.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dashi on 15/4/28.
 */

public class GsonHelper {
    static JsonSerializer<Date> dateSerializer = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            if(src == null) return null;
            long tzOffset = src.getTimezoneOffset();
            String tz = (tzOffset >= 0 ? "+" : "-") + (tzOffset / 60) + (tzOffset % 60);

            return src == null ? null : new JsonPrimitive("/Date(" + src.getTime() + tz + ")/");
        }
    };
    static Pattern datePattern = Pattern.compile("/Date\\((\\d+)");

    static JsonDeserializer<Date> dateDeserializer = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            if(json == null || json.isJsonNull()) return null;
            String s = json.getAsString();
            if(s == null )return null;
            Matcher m = datePattern.matcher(s);
            if(!m.matches()) return null;
            return new Date(Long.parseLong(m.group(1)));
        }
    };

    public static class SpecificClassExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            if (clazz == null) return false;
            return shouldSkipClass(clazz.getSuperclass());
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            // return f.getDeclaringClass() == Model.class;
            return false;
        }

    }
    static Gson gson;
    public static Gson getGson(){
        if(gson == null)
            gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, dateSerializer)
                    .registerTypeAdapter(Date.class, dateDeserializer)
                    .setExclusionStrategies(new SpecificClassExclusionStrategy())
                    .create();
        return gson;
    }
}
