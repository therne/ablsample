package org.airbloc.airblocsample2;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.danlew.android.joda.JodaTimeAndroid;

import org.airbloc.airblocsample2.app.MigrationManager;
import org.airbloc.airblocsample2.app.settings.Settings;
import org.airbloc.airblocsample2.datamgmt.DataCache;
import org.airbloc.airblocsample2.datamgmt.DataStore;
import org.airbloc.airblocsample2.rest.RestClient;
import org.airbloc.airblocsample2.rest.Session;
import org.airbloc.airblocsample2.views.FontCache;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * The abstraction of the app's entire lifecycle.
 * Initializes some modules.
 */
public class App extends Application {

    private static App context;
    private static Gson gson;

    @SuppressWarnings("PointlessBooleanExpression")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // init
        initGson();
        RestClient.init();
        Session.init();
        Settings.init(this);
        FontCache.init(this);
        DataStore.init(this);
        DataCache.init(this);
        JodaTimeAndroid.init(this);

        // Migrate
        MigrationManager migrator = new MigrationManager(this);
        migrator.migrate();
    }

    private static void initGson() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                    throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.getTime());
            }
        });
        gson = builder.create();
    }

    /**
     * @return The application context
     */
    public static App getContext() {
        return context;
    }

    /**
     * @return The GSON instance
     */
    public static Gson getGson() {
        return gson;
    }
}
