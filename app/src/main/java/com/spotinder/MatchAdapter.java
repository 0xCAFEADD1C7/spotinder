package com.spotinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotinder.entities.Match;

import java.util.ArrayList;

public class MatchAdapter extends ArrayAdapter<Match> {
    public MatchAdapter(Context context, ArrayList<Match> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Match match = getItem(position);

        ImageView picture = null;
        TextView name = null;
        TextView title = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_entity , parent, false);
            picture = convertView.findViewById(R.id.personPicture);
            name = convertView.findViewById(R.id.personName);
            title = convertView.findViewById(R.id.matchTitle);
        }

        picture.setImageDrawable(match.getPicture());
        name.setText(match.getName());
        title.setText(match.getMatchTitle());

        // Return the completed view to render on screen
        return convertView;
    }
}