package com.thebengalstudio.authentication.Service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    private static final String MY_PRE_NAME = "com.thebengalstudio.auth";

    public static void SaveInSharePref(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PRE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key,value);
        myEdit.commit();
    }

    public static String LoadSharePref(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PRE_NAME,context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"").toString();

    }

    public static void RemoveSharePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PRE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.clear();
        myEdit.commit();
    }
}
