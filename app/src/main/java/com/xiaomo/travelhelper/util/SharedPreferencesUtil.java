package com.xiaomo.travelhelper.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * SharedPreferences 工具类
 */

public class SharedPreferencesUtil {

    private static final String FILE_NAME = "travel_helper";

    public static void save(Context context,String key,String val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(key,val);
        editor.apply();
    }

    public static void save(Context context,String key,int val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(key,val);
        editor.apply();
    }


    public static void save(Context context,String key,float val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putFloat(key,val);
        editor.apply();
    }

    public static void save(Context context,String key,boolean val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(key,val);
        editor.apply();
    }

    public static void save(Context context,String key,long val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(key,val);
        editor.apply();
    }

    public static void save(Context context,String key,Set<String> val){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putStringSet(key,val);
        editor.apply();
    }

    public static String read(Context context,String key,String defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getString(key,defaultVal);

    }

    public static int read(Context context,String key,int defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getInt(key,defaultVal);

    }

    public static float read(Context context,String key,float defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getFloat(key,defaultVal);

    }

    public static long read(Context context,String key,long defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getLong(key,defaultVal);

    }

    public static boolean read(Context context,String key,boolean defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getBoolean(key,defaultVal);

    }

    public static Set<String> read(Context context,String key,Set<String> defaultVal){

        SharedPreferences sharedPreference = context.getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        return sharedPreference.getStringSet(key,defaultVal);

    }


}
