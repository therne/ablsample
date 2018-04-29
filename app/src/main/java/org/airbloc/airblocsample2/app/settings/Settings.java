package org.airbloc.airblocsample2.app.settings;

import android.content.Context;
import android.content.SharedPreferences;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.rest.Session;

import org.airbloc.airblocsample2.rest.Session;

/**
 * 앱의 설정을 저장한다.
 */
public class Settings {
    private static final String PREF_NAME = "settings";
    private static final String KEY_PUSH_ENABLED = "push_enabled";
    private static final boolean PUSH_ENABLED_DEFAULT = true;
    private static final String KEY_FIRST_GOAL = "first_goal";
    private static final String KEY_VERSION = "version";
    private static final boolean FISRT_GOAL_DEFAULT = true;

    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFirstGoal() {
        return prefs.getBoolean(KEY_FIRST_GOAL, FISRT_GOAL_DEFAULT);
    }

    public static void setFirstGoal() {
        prefs.edit().putBoolean(KEY_FIRST_GOAL, false).apply();
    }

    public static int getVersion() {
        return prefs.getInt(KEY_VERSION,
                Session.isLoggedIn() ? 0 : Constants.VERSION_CODE);
    }

    public static void updateVersion() {
        prefs.edit().putInt(KEY_VERSION, Constants.VERSION_CODE).apply();
    }

    public static boolean isPushEnabled() {
        return prefs.getBoolean(KEY_PUSH_ENABLED, PUSH_ENABLED_DEFAULT);
    }

    public static void setPushEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_PUSH_ENABLED, enabled).apply();
    }
}
