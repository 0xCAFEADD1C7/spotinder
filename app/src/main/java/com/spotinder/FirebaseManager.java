package com.spotinder;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.spotinder.entities.TrackInfo;
import com.spotinder.entities.TrackListening;
import com.spotinder.entities.UserInfo;

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

        addCallbacks(lsts.add(tl), "track listening");
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

    public static void saveUserInfos(UserInfo userInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lsts = db.collection("usersInfos");

        String uid = userInfo.getUid();

        Task<Void> task = lsts
                .document(uid)
                .set(userInfo);
        addCallbacks(task, "user infos");

    }

    public static void saveTrackInfo(TrackInfo info) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lsts = db.collection("trackInfos");
        Task<Void> t = lsts
                .document(info.getId())
                .set(info);
        addCallbacks(t, "track infos");
    }

    public static <T> void addCallbacks(Task<T> task, final String objName){
        task
                .addOnSuccessListener(new OnSuccessListener<T>() {
                    @Override
                    public void onSuccess(T aVoid) {
                        Log.d(TAG, "onSuccess: "+objName+" added/set in firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: Cannot update "+objName, e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<T>() {
                    @Override
                    public void onComplete(@NonNull Task<T> task) {
                        Log.d(TAG, "onComplete: adding "+objName+" to firebase complete");
                    }
                });
    }
}
