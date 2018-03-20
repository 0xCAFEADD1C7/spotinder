package com.spotinder.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aymeric on 13/03/2018.
 */

public class TrackListening {
    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TrackListening () {

    }

    private String trackId;
    private String userId;
    private Date date;

    public TrackListening(String trackId, String userId, Date date) {
        this.trackId = trackId;
        this.userId = userId;
        this.date = date;
    }

    @Override
    public String toString() {
        return "user #"+userId+" listened to track #"+trackId+" on "+date;
    }
}
