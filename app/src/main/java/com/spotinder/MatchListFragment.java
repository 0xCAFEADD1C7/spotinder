package com.spotinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.spotinder.entities.Match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aymeric on 19/03/2018.
 */

public class MatchListFragment extends Fragment {
    private static final String TAG = "MatchListFragment";
    private static final String MATCHES_KEY = "matches_key";

    MatchAdapter adapter;

    public static Fragment newInstance(List<Match> matches) {
        Log.d(TAG, "newInstance: here");
        Fragment fragment = new MatchListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MATCHES_KEY, (Serializable) matches);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Match> matches = null;

        Log.d(TAG, "onCreate: ");

        if (getArguments() != null) {
            matches = (ArrayList<Match>) getArguments().getSerializable(MATCHES_KEY);
        }

        String myId = Preferences.getPrefs(getContext()).getString(Preferences.PREF_SPOTIFY_UID, null);
        Query myMatches = FirebaseFirestore.getInstance()
                .collection("match")
//                .orderBy("date")
//                .limit(50)
                .document(myId)
                .collection("matches");

//        myMatchesDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                Log.d(TAG, "onEvent: "+documentSnapshot.toObject(Match.class));
//            }
//        });

        adapter = new MatchAdapter(getContext(), matches == null ? new ArrayList<Match>() : matches);

        myMatches.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: snapshot listner error", e);
                    return;
                }

                List<Match> matches = queryDocumentSnapshots.toObjects(Match.class);
                adapter.clear();
                adapter.addAll(matches);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View newView = inflater.inflate(R.layout.match_list, container, false);
        ListView list = newView.findViewById(R.id.listMatches);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMatch(adapter.getItem(position));
            }
        });

        return newView;
    }

    protected void showMatch(Match match) {
        Fragment fragment = MatchFragment.newInstance(match);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}