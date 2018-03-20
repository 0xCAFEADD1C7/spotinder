package com.spotinder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.spotinder.entities.Match;

import java.util.ArrayList;

/**
 * Created by aymeric on 19/03/2018.
 */

public class MatchListFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    MatchAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MatchAdapter(getContext(), new ArrayList<Match>());
        adapter.add(new Match(ContextCompat.getDrawable(getActivity(), R.drawable.girl1), "Marion cotillard", "Losing my religion - REM"));
        adapter.add(new Match(ContextCompat.getDrawable(getActivity(), R.drawable.girl2), "Gaelle Trouduc", "Hangover - Alestorm"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View newView = inflater.inflate(R.layout.match_list, container);
        ListView list = newView.findViewById(R.id.listMatches);
        list.setAdapter(adapter);

        return newView;
    }
}
