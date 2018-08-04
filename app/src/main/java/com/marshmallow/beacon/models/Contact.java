package com.marshmallow.beacon.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;

/**
 * Created by George on 7/24/2018.
 */
public class Contact {
    private String username;
    private String profilePicture;
    private Boolean signedIn;

    public Contact() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Exclude
    public Bitmap getProfilePictureBitmap() {
        if (profilePicture != null) {
            byte[] decodedString = Base64.decode(profilePicture, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            return  null;
        }
    }

    @Exclude
    public void setProfilePictureFromBitmap(Bitmap profilePictureBitmap) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        profilePictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        profilePicture = Base64.encodeToString(b, Base64.DEFAULT);
    }

}
