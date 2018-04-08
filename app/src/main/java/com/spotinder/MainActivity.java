package com.spotinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotinder.entities.Match;
import com.spotinder.entities.UserInfo;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

// matchSave(List<Match>)
// matchLoad()

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    SpotifyService spotifyService;
    SpotifyApi spotifyApi = new SpotifyApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.titleBar));

        // if user is logged in, we load his list from disk.
        // then try to refresh info from firestore

        SharedPreferences prefs = Preferences.getPrefs(getApplicationContext());
        String uid = prefs.getString(Preferences.PREF_SPOTIFY_UID, null);

        // user logged in
        if (uid != null) {
            Log.d(TAG, "onCreate: showing list of matches");
//            List<Match> lMatches = MatchesPersistence.load();
            List<Match> lMatches = new ArrayList<>();
            showMatchesList(lMatches);
        } else { // user not logged in
            Log.d(TAG, "onCreate: display login btn");
            setContentView(R.layout.login_view);
            Button btn = findViewById(R.id.loginButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestLogin();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                displaySettings();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displaySettings() {
        Intent start = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(start);
    }

    private static final int REQUEST_CODE = 1337;
    private static final String[] SPOTIFY_SCOPES = new String[]{
            "user-read-private","user-read-email","user-read-birthdate"
    };

    // lance la procedure de login avec Spotify
    public void requestLogin(){
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        Context ctx = getApplicationContext();
        String redirectUri =
                ctx.getString(R.string.com_spotify_sdk_redirect_scheme)
                + "://" +
                ctx.getString(R.string.com_spotify_sdk_redirect_host);
        String clientId = ctx.getString(R.string.com_spotify_client_id);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);

        builder.setScopes(SPOTIFY_SCOPES);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginInBrowser(this, request);
    }

    // affiche la liste des matches
    protected void showMatchesList(List<Match> matches) {
        Log.d(TAG, "showMatchesList: ");
        Fragment fragment = MatchListFragment.newInstance(matches);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.commit();
    }

    // appelée lorsque l'on recoit un Intent:
    // ici : uniquement appelé lors de l'auth Spotify
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: HERE");

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);
            Log.d(TAG, "onNewIntent: response ok");

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.d(TAG, "onNewIntent: received token !!");
                    Log.d(TAG, "onNewIntent: response : "+response.getAccessToken());

                    spotifyApi = new SpotifyApi();
                    spotifyApi.setAccessToken(response.getAccessToken());

                    spotifyService = spotifyApi.getService();
                    spotifyService.getMe(new Callback<UserPrivate>() {
                        @Override
                        public void success(UserPrivate user, Response response) {
                            toastOk(R.string.login_ok, " "+user.display_name+" !");
                            onUserInfo(user);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            toastError(R.string.spotify_api_error, error);
                        }
                    });

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    toastError(R.string.spotify_auth_error, new Exception(response.getError()));
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d(TAG, "onNewIntent: default switch case");
                    // Handle other cases
            }
        }
    }

    protected void onUserInfo(UserPrivate userInfo) {
        Log.d(TAG, "onUserInfo: here");

        final String uid = userInfo.id;
        final String dispName = userInfo.display_name;
        final String email = userInfo.email;

        final String pictureUrl = userInfo.images.size() > 0
                ? userInfo.images.get(0).url
                : "https://cdn3.iconfinder.com/data/icons/rcons-user-action/32/girl-512.png";

        FirebaseManager.getUserInfo(uid, new OnSuccessListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                Context context = getApplicationContext();
                SharedPreferences sharedPref = Preferences.getPrefs(context);

                // if user has no preferences (= first login), create default ones
                if (userInfo == null) {
                    Log.d(TAG, "onSuccess: no user info.");
                    userInfo = new UserInfo();
                    userInfo.setContactType("EMAIL");
                    userInfo.setUid(uid);
                } else {
                    Log.d(TAG, "onSuccess: got user infos "+userInfo);
                }

                // update some settings from spotify api
                userInfo.setEmail(email);
                userInfo.setDispName(dispName);
                userInfo.setPictureUrl(pictureUrl);

                Preferences.saveUserInfo(userInfo, context, true);
            }
        });
    }

    protected void toastError(int stringId, Exception error) {
        Context appContext = getApplicationContext();
        String errMsg = appContext.getString(stringId);
        Toast.makeText(appContext, errMsg + " : " + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void toastOk(int stringId, String additional) {
        Context appContext = getApplicationContext();
        String msg = appContext.getString(stringId);
        Toast.makeText(appContext, msg + " : " + additional, Toast.LENGTH_SHORT).show();
    }
}
