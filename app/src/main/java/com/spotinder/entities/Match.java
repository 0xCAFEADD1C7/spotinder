package com.spotinder.entities;

import android.graphics.drawable.Drawable;

/**
 * Created by aymeric on 15/03/2018.
 */

public class Match {
    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public Match(Drawable picture, String name, String matchTitle) {
        this.picture = picture;
        this.name = name;
        this.matchTitle = matchTitle;
    }

    private Drawable picture;
    private String name;
    private String matchTitle;
}
