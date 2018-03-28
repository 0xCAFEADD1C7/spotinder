package com.spotinder;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.spotinder.entities.TrackListening;

import java.util.Date;

/**
 * Created by aymeric on 20/03/2018.
 */

public class FirebaseManager {
    static final String TAG = "FirebaseManager";

    public static void addTrackListening(String trackId, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lsts = db.collection("trackListening");

        TrackListening tl = new TrackListening(trackId, userId, new Date());

        lsts
                .add(tl)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d(TAG, "onComplete: conplete.");
                    }
                });
    }

    public void getMatches() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("trackListening").whereEqualTo("trackId", "aeb20452")
                .whereGreaterThan("date", new Date(new Date().getTime() - 7*3600*24*1000))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException
                            e) {
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
