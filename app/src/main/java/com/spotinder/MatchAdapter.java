package com.spotinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spotinder.entities.Match;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchAdapter extends ArrayAdapter<Match> {
    public MatchAdapter(Context context, ArrayList<Match> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Match match = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_entity , parent, false);
        }

        //TODO change to holder
        CircleImageView picture = convertView.findViewById(R.id.personPicture);
        TextView name = convertView.findViewById(R.id.personName);
        TextView title = convertView.findViewById(R.id.matchTitle);

        String pictureUrl = match.getPicture();
        if (pictureUrl == null || pictureUrl.isEmpty()) {
            pictureUrl = "https://flymark.com.ua/Content/no-photo.jpg";
        }

        Picasso.get().load(pictureUrl).into(picture);
        name.setText(match.getName());
        title.setText(match.getMatchTitle());

        // Return the completed view to render on screen
        return convertView;
    }
}