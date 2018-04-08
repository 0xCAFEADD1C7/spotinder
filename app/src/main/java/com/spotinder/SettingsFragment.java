package com.spotinder;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.spotinder.entities.UserInfo;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    private boolean isValidNumber(String number) {
        return number.matches("\\+?\\d*");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getPreferenceScreen().findPreference("PREF_PHONE").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (isValidNumber(newValue.toString())) {
                    Log.d(TAG, "onPreferenceChange: valid");
                    return true;
                } else {
                    Log.d(TAG, "onPreferenceChange: invalid");
                    return false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UserInfo ui = Preferences.getUserInfo(getContext());

        Preferences.saveUserInfo(ui, getContext(), true);
    }
}