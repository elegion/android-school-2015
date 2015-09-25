package com.elegion.githubclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Artem Mochalov.
 */
public class AppDelegate extends Application {

    private static final String PREFERENCES_NAME = "app_preferences";
    private static SharedPreferences sSharedPreferences;
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Override
    public void onCreate() {
        super.onCreate();
        setPreferences();
    }

    public static SharedPreferences getSettings() {
        return sSharedPreferences;
    }

    private void setPreferences() {
        sSharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
