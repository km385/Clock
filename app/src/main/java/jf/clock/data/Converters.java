package jf.clock.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {
    private static final String TAG = "Converters";
    @TypeConverter
    public static boolean[] fromString(String value){
        Gson gson = new Gson();
        Type type = new TypeToken<boolean[]>() {}.getType();

        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String fromArray(boolean[] value){
        Gson gson = new Gson();
        return gson.toJson(value);
    }
}
