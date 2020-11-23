package com.akrasnoyarov.instaclone;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {
    public static final String APP_PREFERENCES_FILE_NAME = "com.akrasnoyarov.instaclone.userdata";
    public static final String USER_NAME = "username";
    public static final String USER_TOKEN = "user_token";
    public static final String OAUTH_TOKEN = "oauth_token";

    private SharedPreferences preferences;

    public ApplicationPreferences(Context context) {
        preferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

}
