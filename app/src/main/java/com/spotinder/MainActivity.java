package com.spotinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.titleBar));

        startLogin();
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
    private static final String[] SPOTIFY_SCOPES = new String[]{"streaming"};

    public void startLogin(){
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult: here");

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: correctly authed request code");
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);


        }
    }

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
                    Log.d(TAG, "onActivityResult: received token !!");
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e(TAG, "onActivityResult: oups, error case !n"+response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d(TAG, "onActivityResult: default switch case");
                    // Handle other cases
            }
        }
    }
}
