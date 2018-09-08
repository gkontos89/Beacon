package com.marshmallow.beacon.models;

import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.models.user.User;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class UserManagerTest {

    @Test
    public void singletonTest() {
        UserManager userManager1 = UserManager.getInstance();
        UserManager userManager2 = UserManager.getInstance();
        assertEquals(userManager1, userManager2);
    }

    @Test
    public void userTest() {
        User user = new User();
        UserManager.getInstance().setUser(user);
        assertEquals(user, UserManager.getInstance().getUser());
    }



}
