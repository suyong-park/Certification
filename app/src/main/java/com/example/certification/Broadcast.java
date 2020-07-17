package com.example.certification;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

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

    static MaterialAlertDialogBuilder AlertBuild(Activity activity, String title, String message) {
        return new MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
    }

    static boolean isNetworkConnected(Activity activity) { // Checking the Network is Connected
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    static void isNetworkWorking(final Activity activity, final boolean ismain) {
        if (!Broadcast.isNetworkConnected(activity)) {
            Broadcast.AlertBuild(activity, "메시지", "네트워크 연결 상태를 확인해 주세요.")
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(ismain)
                                return;
                            activity.finish();
                        }
                    })
                    .show();
            return ;
        }
    }

    static String changeSearch(String search_word) {
        String query = "";
        if(search_word.equals("자격증"))
            query = "select name from certification";
        else
            query = "select name from certification where name like " + "'%" + search_word + "%'";
        return query;
    }

    private static HashMap<String, String> map_certification;    // value is an hashmap.
    static void HashMap() {

        map_certification = new HashMap<>();

        map_certification.put("정처기", "정보처리기사");
        map_certification.put("gtq", "GTQ 1급");
        map_certification.put("GTQ", "GTQ 1급");
        map_certification.put("Gtq", "GTQ 1급");
        //map_certification.put("웹", "");
    }

    static String Filtering(String search_word) {
        if (map_certification.containsKey(search_word))
            search_word = map_certification.get(search_word);  // TODO : 필터링 기능 수행 해당 요소의 value 값을 가져온다.

        return changeSearch(search_word);
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
