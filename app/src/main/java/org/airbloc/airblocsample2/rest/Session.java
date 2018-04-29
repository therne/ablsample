package org.airbloc.airblocsample2.rest;

import android.content.Context;
import android.content.SharedPreferences;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.models.User;
import org.airbloc.airblocsample2.rest.results.TokenResult;

import org.airbloc.airblocsample2.rest.results.TokenResult;

/**
 * Saves user's session and informations to the storage.
 */
public class Session {
    private static final String PREF_NAME = "sessions";

    private static User me;
    private static SharedPreferences prefs;
    private static String accessToken, refreshToken;

    public static void init() {
        prefs = App.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        accessToken = prefs.getString("access_token", null);
        refreshToken = prefs.getString("refresh_token", null);

        if (accessToken != null) {
            // load user
            String userJson = prefs.getString("me", null);
            if (userJson != null) me = User.fromJson(userJson);
        }
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void saveTempSession() {
        accessToken = "temp";
    }

    public static void saveSession(TokenResult tokens) {
        accessToken = tokens.accessToken;
        refreshToken = tokens.refreshToken;
        Logger.e(tokens.toString());

        prefs.edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .apply();
    }

    public static void saveUser(User user) {
        Logger.e(user.toString());
        me = user;
        prefs.edit()
                .putString("me", App.getGson().toJson(me))
                .apply();
    }

    public static void refreshSession(String newAccessToken) {
        accessToken = newAccessToken;

        prefs.edit()
            .putString("access_token", newAccessToken)
            .apply();
    }

    public static void clearSession() {
        me = null;
        accessToken = refreshToken = null;
        prefs.edit().clear().apply();
    }

    public static User getMe() {
        return me;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }
}
