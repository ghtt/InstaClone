package com.akrasnoyarov.instaclone.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.akrasnoyarov.instaclone.MainActivity;
import com.akrasnoyarov.instaclone.R;
import com.akrasnoyarov.instaclone.SettingsActivity;

import static com.akrasnoyarov.instaclone.ApplicationPreferences.APP_PREFERENCES_FILE_NAME;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey);
        initializePreferences(getPreferenceScreen());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference != null) {

            String value = sharedPreferences.getString(key, "");
            preference.setSummary(value);
        }
    }

    private void initializePreferences(PreferenceScreen preferenceScreen) {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        Log.d("myLogs", "preferences count " + count);
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            Log.d("myLogs", p.getKey());
           switch(p.getKey()) {
               case "username":
                p.setSummary(SettingsActivity.sUsername);
               case "logoutButton":
                   p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                       @Override
                       public boolean onPreferenceClick(Preference preference) {
                           Log.d("myLogs", "clicked");

                           // delete all saved data... BAD!!! solution.......
                           SharedPreferences.Editor editor = getContext().getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit();
                           editor.putString("username", "");
                           editor.putString("user_token", "");
                           editor.putString("oauth_token", "");
                           editor.apply();
                           MainActivity.sAuthToken = "";
                           MainActivity.sUserToken = "";

                           return true;
                       }
                   });
            }

        }
    }
}
