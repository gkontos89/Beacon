package com.marshmallow.beacon.models;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/17/2018.
 */
public class User {

    private String username;
    private Boolean signedIn;
    private Integer points;
    private DataPoint firstName;
    private DataPoint lastName;
    private DataPoint email;
    private DataPoint birthday;
    private DataPoint city;
    private DataPoint state;
    private Rolodex rolodex;

    public User () {
    }

    public User(String username) {
        this.username = username;
        this.signedIn = true;
        this.points = 0;
        this.firstName = new DataPoint(null, false);
        this.lastName = new DataPoint(null, false);
        this.email = new DataPoint(null, false);
        this.birthday = new DataPoint(null, false);
        this.city = new DataPoint(null, false);
        this.state = new DataPoint(null, false);
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
}
