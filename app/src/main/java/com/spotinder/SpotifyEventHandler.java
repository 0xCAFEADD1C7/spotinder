package com.spotinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SpotifyEventHandler extends BroadcastReceiver {
    static final String TAG = "SpotifyEventHandler";

    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
        static final String QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long evtDate = intent.getLongExtra("timeSent", 0L);

        String action = intent.getAction();

        if (action.equals(BroadcastTypes.METADATA_CHANGED)) {
            String trackId = intent.getStringExtra("id");
            String artistName = intent.getStringExtra("artist");
            String albumName = intent.getStringExtra("album");
            String trackName = intent.getStringExtra("track");

            //TODO change user id
            FirebaseManager.addTrackListening(trackId, "123456");

            Log.d(TAG, "Track info : "+trackId+", "+artistName+", "+albumName+", "+trackName);
        }


    }
}