package com.marshmallow.beacon.models.user;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/17/2018.
 */
public class Rolodex {

    private List<String> usernames;

    public Rolodex() {
        usernames = new Vector<>();
    }

    public List<String> getUsernames() { return usernames; }
    public void removeUsername(String username) { usernames.remove(username); }
    public void addUsername(String username) { usernames.add(username); }
}
