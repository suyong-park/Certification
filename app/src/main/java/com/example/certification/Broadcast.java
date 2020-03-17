package com.example.certification;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Broadcast {

    static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ConnectDB.IP_ADDRESS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

class PreferenceManager {

    public static final String PREFERENCES_NAME = "rebuild_preference";
    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INTEGER = -1;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // save String value
    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // save Integer value
    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // save Boolean value
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    // load String value
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    // load Boolean value
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;
    }

    // load Integer value
    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INTEGER);
        return value;
    }

    // delete key
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    // delete data about PreperenceManager
    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
