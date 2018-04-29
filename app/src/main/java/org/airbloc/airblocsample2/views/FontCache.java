package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.WeakHashMap;

/**
 * Caches font.
 */
public class FontCache {
    private static final WeakHashMap<String, Typeface> TYPEFACE_CACHE = new WeakHashMap<>();
    private static AssetManager assetManager;

    public static void init(Context context) {
        assetManager = context.getAssets();
    }

    public static Typeface get(String name) {
        if (TYPEFACE_CACHE.containsKey(name)) return TYPEFACE_CACHE.get(name);
        Typeface font = Typeface.createFromAsset(assetManager, name);
        TYPEFACE_CACHE.put(name, font);
        return font;
    }
}
