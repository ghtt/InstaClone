package com.akrasnoyarov.instaclone.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.akrasnoyarov.instaclone.R;
import com.akrasnoyarov.instaclone.SettingsActivity;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey);
        setPreferencesSummary(getPreferenceScreen());
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

    private void setPreferencesSummary(PreferenceScreen preferenceScreen) {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (p instanceof EditTextPreference) {
                p.setSummary(SettingsActivity.sUsername);
            }
        }
    }
}
