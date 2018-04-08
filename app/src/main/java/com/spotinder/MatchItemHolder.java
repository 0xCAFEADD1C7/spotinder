package com.spotinder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aymeric on 05/04/2018.
 */

class MatchItemHolder extends RecyclerView.ViewHolder {
    CircleImageView personPicture;
    TextView personName;
    TextView matchTitle;

    public MatchItemHolder(View v) {
        super(v);

        personPicture = v.findViewById(R.id.personPicture);
        personName = v.findViewById(R.id.personName);
        matchTitle = v.findViewById(R.id.matchTitle);
    }
}
