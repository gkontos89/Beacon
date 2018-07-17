package com.marshmallow.beacon;

import com.marshmallow.beacon.models.User;

/**
 * Created by George on 7/17/2018.
 */
public class UserManager {
    private static UserManager instance = null;
    private User user;

    private UserManager() {
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
}
