package com.spotinder.entities;

import java.io.Serializable;

/**
 * Created by aymeric on 29/03/2018.
 */

public class UserInfo implements Serializable {
    private String uid;
    private String dispName;
    private String phone;
    private String email;
    private String pictureUrl;
    private String matchPref;
    private String sex;
    private String contact;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getMatchPref() {
        return matchPref;
    }

    public void setMatchPref(String matchPref) {
        this.matchPref = matchPref;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
