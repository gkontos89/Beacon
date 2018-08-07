package com.marshmallow.beacon.models.marketing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;


/**
 * Created by George on 8/5/2018.
 */
public class Sponsor {

    private String uid;
    private String name;
    private String picture;
    private String url;

    public Sponsor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Exclude
    public Bitmap getProfilePictureBitmap() {
        if (picture != null) {
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return  null;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
