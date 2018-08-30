package com.marshmallow.beacon.models.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.Exclude;

import java.io.ByteArrayOutputStream;

/**
 * Created by George on 7/17/2018.
 */
public class User {

    private String username;
    private String profilePicture;
    private Boolean signedIn;
    private Integer points;
    private DataPoint geolocationOn;
    private DataPoint firstName;
    private DataPoint lastName;
    private DataPoint email;
    private DataPoint birthday;
    private DataPoint city;
    private DataPoint state;
    private DataPoint phoneNumber;
    private Boolean accountCreationComplete;
    private Rolodex rolodex;

    public User () {
    }

    public User(String email) {
        this.username = null;
        this.profilePicture = null;
        this.signedIn = true;
        this.points = 0;
        this.geolocationOn = new DataPoint(null, false);
        this.firstName = new DataPoint(null, false);
        this.lastName = new DataPoint(null, false);
        this.email = new DataPoint(email, false);
        this.birthday = new DataPoint(null, false);
        this.city = new DataPoint(null, false);
        this.state = new DataPoint(null, false);
        this.phoneNumber = new DataPoint(null, false);
        this.accountCreationComplete = false;
        rolodex = new Rolodex();
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public DataPoint getFirstName() {
        return firstName;
    }

    public void setFirstName(DataPoint firstName) {
        this.firstName = firstName;
    }

    public DataPoint getLastName() {
        return lastName;
    }

    public void setLastName(DataPoint lastName) {
        this.lastName = lastName;
    }

    public DataPoint getEmail() {
        return email;
    }

    public void setEmail(DataPoint email) {
        this.email = email;
    }

    public DataPoint getBirthday() {
        return birthday;
    }

    public void setBirthday(DataPoint birthday) {
        this.birthday = birthday;
    }

    public DataPoint getCity() {
        return city;
    }

    public void setCity(DataPoint city) {
        this.city = city;
    }

    public DataPoint getState() {
        return state;
    }

    public void setState(DataPoint state) {
        this.state = state;
    }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Rolodex getRolodex() { return rolodex; }
    public void setRolodex(Rolodex rolodex) { this.rolodex = rolodex; }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public DataPoint getGeolocationOn() {
        return geolocationOn;
    }

    public void setGeolocationOn(DataPoint geolocationOn) {
        this.geolocationOn = geolocationOn;
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

    public DataPoint getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(DataPoint phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAccountCreationComplete() {
        return accountCreationComplete;
    }

    public void setAccountCreationComplete(Boolean accountCreationComplete) {
        this.accountCreationComplete = accountCreationComplete;
    }
}
