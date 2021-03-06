package com.marshmallow.beacon.models.marketing;

import android.support.annotation.NonNull;

import com.marshmallow.beacon.models.user.User;

/**
 * Created by George on 8/5/2018.
 */
public class UserMarketDataSnapshot {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String city;
    private String state;
    private String phone;

    public UserMarketDataSnapshot() {
    }

    public UserMarketDataSnapshot(@NonNull User user) {
        username = user.getUsername();
        if (user.getFirstName().getShared()) { firstName = user.getFirstName().getValue(); }
        if (user.getLastName().getShared()) { lastName = user.getLastName().getValue(); }
        if (user.getEmail().getShared()) { email = user.getEmail().getValue(); }
        if (user.getBirthday().getShared()) {birthday = user.getBirthday().getValue(); }
        if (user.getCity().getShared()) { city = user.getCity().getValue(); }
        if (user.getState().getShared()) { state = user.getState().getValue(); }
        if (user.getPhoneNumber().getShared()) {
            phone = user.getPhoneNumber().getValue();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
