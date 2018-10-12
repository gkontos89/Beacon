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
    private Boolean geolocationOn;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String city;
    private String state;
    private String phoneNumber;
    private Boolean accountCreationComplete;
    private Rolodex rolodex;

    public User () {
    }

    public User(String email) {
        this.username = null;
        this.profilePicture = null;
        this.signedIn = true;
        this.points = 0;
        this.geolocationOn = null;
        this.firstName = null;
        this.lastName = null;
        this.email = email;
        this.birthday = null;
        this.city = null;
        this.state = null;
        this.phoneNumber = null;
        this.accountCreationComplete = false;
        rolodex = new Rolodex();
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
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

    public Boolean getGeolocationOn() {
        return geolocationOn;
    }

    public void setGeolocationOn(Boolean geolocationOn) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAccountCreationComplete() {
        return accountCreationComplete;
    }

    public void setAccountCreationComplete(Boolean accountCreationComplete) {
        this.accountCreationComplete = accountCreationComplete;
    }
}
