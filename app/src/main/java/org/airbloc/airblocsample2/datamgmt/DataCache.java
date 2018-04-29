package org.airbloc.airblocsample2.datamgmt;

import android.content.Context;
import android.content.SharedPreferences;

import org.airbloc.airblocsample2.App;

import org.airbloc.airblocsample2.App;

/**
 * 데이터를 Pin하기위해 사용한다.
 */
public class DataCache {
    private static final String XML_NAME = "data_cache";
    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
    }

    public static boolean hasCache(String key) {
        return prefs.contains(key);
    }

    public static void store(String key, Object data) {
        prefs.edit()
                .putString(key, App.getGson().toJson(data))
                .apply();
    }

    public static void clear() {
        prefs.edit().clear().apply();
    }

    public static <T> T get(String key, Class<T> type) {
        return !hasCache(key) ? null : App.getGson().fromJson(prefs.getString(key, "{}"), type);
    }
}
