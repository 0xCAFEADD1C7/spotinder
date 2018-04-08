package com.spotinder;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spotinder.entities.Match;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchFragment extends Fragment {
    private static final String MATCH_DATA_KEY = "match_data_key";
    private Match matchData;

    private static final String TAG = "MatchFragment";

    public MatchFragment() {
        // Required empty public constructor
    }

    public static MatchFragment newInstance(Match match) {
        MatchFragment fragment = new MatchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MATCH_DATA_KEY, match);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchData = (Match) getArguments().getSerializable(MATCH_DATA_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        Log.d(TAG, "onCreateView: ON EST LA");

        TextView nameTv = view.findViewById(R.id.nameTextView);
        nameTv.setText(matchData.getName());

        TextView titleTv = view.findViewById(R.id.matchTitleTextView);
        titleTv.setText(matchData.getMatchTitle());

        String pictureUrl = matchData.getPicture();
        if (pictureUrl == null || pictureUrl.isEmpty()) {
            pictureUrl = "https://flymark.com.ua/Content/no-photo.jpg";
        }
        CircleImageView picView = view.findViewById(R.id.matchImageView);
        Picasso.get().load(pictureUrl).into(picView);

        Button btn = view.findViewById(R.id.contact_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact(matchData);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    public void contact(Match match) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", match.getContactData(), null));
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
