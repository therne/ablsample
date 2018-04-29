package org.airbloc.airblocsample2;

import android.util.Log;

/**
 * Logger
 */
@SuppressWarnings("PointlessBooleanExpression")
public class Logger {
    private static final String TAG = "Airshop";

    public static void e(Exception e) {
        Log.e(TAG, e.getMessage(), e);
    }

    public static void e(String msg, Object ...args) {
        msg = String.format(msg, args);
        if (Constants.DEBUG) Log.e(TAG, msg);
    }


    public static void d(String msg, Object ...args) {
        if (Constants.DEBUG) {
            msg = String.format(msg, args);
            Log.d(TAG, msg);
        }
    }
}
