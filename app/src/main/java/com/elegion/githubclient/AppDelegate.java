package com.elegion.githubclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Artem Mochalov.
 */
public class AppDelegate extends Application {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String USER_NAME = "USER_NAME";
    private static final String PREFERENCES_NAME = "app_preferences";
    private static SharedPreferences sSharedPreferences;

    public static SharedPreferences getSettings() {
        return sSharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setPreferences();
    }

    private void setPreferences() {
        sSharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
