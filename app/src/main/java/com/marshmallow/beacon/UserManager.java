package com.marshmallow.beacon;

import com.marshmallow.beacon.models.contacts.Contact;
import com.marshmallow.beacon.models.user.User;

import java.util.HashMap;

/**
 * Created by George on 7/17/2018.
 */
public class UserManager {
    private static UserManager instance = null;
    private User user;
    private HashMap<String, Contact> contacts;

    private UserManager() {
        contacts = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }
}
