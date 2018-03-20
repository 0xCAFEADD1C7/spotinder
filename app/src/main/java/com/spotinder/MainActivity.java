package com.spotinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.spotinder.entities.TrackListening;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.titleBar));
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

                Log.d("DEBUG", "onOptionsItemSelected: click on settings");
                displaySettings();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displaySettings() {
        Intent start = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(start);
    }

    public void firebaseTest() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trackListening")
                .whereEqualTo("trackId", "aeb20452")
                .whereGreaterThan("date", new Date(new Date().getTime() - 7*3600*24*1000))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (documentSnapshots == null) {
                            Log.e("E", e.getMessage());
                            return;
                        }
                        for (DocumentSnapshot doc : documentSnapshots.getDocuments()){
                            Log.d("DEBUGEE", doc.toObject(TrackListening.class).toString());
                        }
                    }
                });
    }

}
