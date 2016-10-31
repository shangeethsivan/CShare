package com.shrappz.contactsharer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created on 20-10-2016.
 */

public class PrefConnector {
    public static void writeBoolean(Context c, String key, Boolean b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static Boolean readBoolean(Context c, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void writeSting(Context c, String key, String b) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, b);
        editor.commit();
    }

    public static String readString(Context c, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(key, "");
    }


}

