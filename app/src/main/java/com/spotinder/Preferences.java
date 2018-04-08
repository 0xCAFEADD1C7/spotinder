package com.spotinder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aymeric on 29/03/2018.
 */

public class Preferences {
    public static final String PREF_DISPLAY_NAME = "PREF_DISPLAY_NAME";
    public static final String PREF_SPOTIFY_UID = "PREF_SPOTIFY_UID";
    public static final String PREF_EMAIL = "PREF_EMAIL";

    public static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(
                ctx.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
    }
}
