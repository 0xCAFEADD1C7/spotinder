package com.spotinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.spotinder.entities.UserInfo;

import java.util.concurrent.Callable;

/**
 * Created by aymeric on 29/03/2018.
 */

public class Preferences {
    // WARNING : this must match with values defined in settings.xml and arrays.xml
    public static final String PREF_DISPLAY_NAME = "PREF_DISPLAY_NAME";
    public static final String PREF_SPOTIFY_UID = "PREF_SPOTIFY_UID";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_CONTACT_TYPE = "PREF_CONTACT_TYPE";
    public static final String PREF_PICTURE_URL = "PREF_PICTURE_URL";
    public static final String PREF_PHONE = "PREF_PHONE";
    public static final String PREF_WANTED_SEX = "PREF_WANTED_SEX";
    public static final String PREF_MY_SEX = "PREF_MY_SEX";

    public static SharedPreferences getPrefs(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
//        return ctx.getSharedPreferences(
//                ctx.getString(R.string.preference_file_key),
//                Context.MODE_PRIVATE
//        );
    }

    public static UserInfo getUserInfo(Context ctx) {
        UserInfo ui = new UserInfo();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        ui.setUid(prefs.getString(Preferences.PREF_SPOTIFY_UID, ""));
        ui.setDispName(prefs.getString(Preferences.PREF_DISPLAY_NAME, ""));
        ui.setEmail(prefs.getString(Preferences.PREF_EMAIL, ""));
        ui.setPhone(prefs.getString(Preferences.PREF_PHONE, ""));
        ui.setPictureUrl(prefs.getString(Preferences.PREF_PICTURE_URL, ""));
        ui.setContactType(prefs.getString(Preferences.PREF_CONTACT_TYPE, ""));
        ui.setSex(prefs.getString(Preferences.PREF_MY_SEX, ""));
        ui.setWantedSex(prefs.getString(Preferences.PREF_WANTED_SEX, ""));

        return ui;
    }

    public static void saveUserInfo(UserInfo ui, Context ctx, boolean updateRemote) {
        SharedPreferences sharedPref = Preferences.getPrefs(ctx);

        String uid = ui.getUid(),
                dispName = ui.getDispName(),
                email = ui.getEmail(),
                pictureUrl = ui.getPictureUrl(),
                contactType = ui.getContactType(),
                sex = ui.getSex(),
                phone = ui.getPhone(),
                wantedSex = ui.getWantedSex();

        // update local preferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Preferences.PREF_SPOTIFY_UID, uid);
        editor.putString(Preferences.PREF_DISPLAY_NAME, dispName);
        editor.putString(Preferences.PREF_EMAIL, email);
        editor.putString(Preferences.PREF_PHONE, phone);
        editor.putString(Preferences.PREF_PICTURE_URL, pictureUrl);
        editor.putString(Preferences.PREF_CONTACT_TYPE, contactType);
        editor.putString(Preferences.PREF_MY_SEX, sex);
        editor.putString(Preferences.PREF_WANTED_SEX, wantedSex);
        editor.commit();

        // update remote preferences
        if (updateRemote) {
            FirebaseManager.saveUserInfos(ui);
        }
    }

    // fill the shared preferences according to the data stored on firestore
    // if not data is available
    public static void fetchRemoteSettings(String uid, Callable<UserInfo> callback) {

    }
}
