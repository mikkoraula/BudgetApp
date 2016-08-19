package com.example.mikko.budgetapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 * Created by Mikko on 29-May-16.
 *
 * Used to easily handle android's sharedPreferences
 */
public class SharedPreferencesHandler {
    /*
    public static void updateSharedPreference(AppCompatActivity activity, String preferenceKey, HashMap<String, String> preferences) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("loginpreferences", activity.MODE_PRIVATE);
        SharedPreferences.Editor loginPreferencesEditor = loginPreferences.edit();
        // not sure if needed to clear the prefs before
        loginPreferencesEditor.clear();

        loginPreferencesEditor.putBoolean("login_remembered", true);
        loginPreferencesEditor.putString("email", email);
        loginPreferencesEditor.putString("password", password);
        loginPreferencesEditor.apply();
    }
    */

    public static SharedPreferences.Editor getEditor(Context context, String preferenceKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    public static String getString(Context context, String preferenceKey, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static Long getLong(Context context, String preferenceKey, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
}
