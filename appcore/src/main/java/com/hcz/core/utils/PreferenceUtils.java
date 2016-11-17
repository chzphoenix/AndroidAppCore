package com.hcz.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class PreferenceUtils {
    private static SharedPreferences sharedPreferences;

    public static final String PREFERENCE_KEY_UUID = "uuid";

    public static void init(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreference(){
        return sharedPreferences;
    }

    private static boolean check(){
        if(sharedPreferences == null){
            Log.e("PreferenceUtils", "SharePreference not init");
            return false;
        }
        return true;
    }

    public static void putBoolean(String key, boolean value) {
        if(check()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static boolean getBoolean(String key)  {
        if(check()) {
            return sharedPreferences.getBoolean(key, false);
        }
        return false;
    }

    public static void putInt(String key, int value) {
        if(check()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static int getInt(String key) {
        if(check()) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }

    public static void putString(String key, String value) {
        if(check()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }


    public static String getString(String key) {
        if(check()) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public static long getLong(String key)  {
        if(check()) {
            return sharedPreferences.getLong(key, 0);
        }
        return 0;
    }

    public static void putLong(String key, long value) {
        if(check()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }


    public static float getFloat(String key) {
        if(check()) {
            return sharedPreferences.getFloat(key, 0);
        }
        return 0;
    }

    public static void putFloat(String key, float value) {
        if(check()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }


}
