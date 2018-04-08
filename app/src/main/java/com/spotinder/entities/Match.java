package com.spotinder.entities;

import java.io.Serializable;

/**
 * Created by aymeric on 15/03/2018.
 */

public class Match implements Serializable {
    private String picture = "";
    private String name = "";
    private String matchTitle = "";
    private String contactType = "";
    private String contactData = "";
    private double random = 0;

    public Match(String picture, String name, String matchTitle) {
        this.picture = picture;
        this.name = name;
        this.matchTitle = matchTitle;
    }

    @Override
    public String toString() {
        return "Match{" +
                "picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", matchTitle='" + matchTitle + '\'' +
                ", contactType='" + contactType + '\'' +
                ", contactData='" + contactData + '\'' +
                '}';
    }

    public Match() {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
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

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactData() {
        return contactData;
    }

    public void setContactData(String contactData) {
        this.contactData = contactData;
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
    }
}
