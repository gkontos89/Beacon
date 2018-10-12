package com.marshmallow.beacon.models.user;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class UserTest {

    private User user = new User();

    @Before
    public void preTest() {
        user.setUsername("gk");
        user.setFirstName("George");
        user.setLastName("Kontos");
        user.setBirthday("6/15/1989");
        user.setCity("Des Plaines");
        user.setState("IL");
        user.setPhoneNumber("847");
        user.setEmail("gk@gmail.com");
    }

    @Test
    public void creationTest() {
        User user = new User("gk@gmail.com");
        assertEquals(null, user.getUsername());
        assertEquals(null, user.getProfilePicture());
        assertEquals(true, user.getSignedIn().booleanValue());
        assertEquals(0, user.getPoints().intValue());
        assertEquals(null, user.getFirstName());

        assertEquals(null, user.getLastName());
        assertEquals("gk@gmail.com", user.getEmail());
        assertEquals(null, user.getBirthday());
        assertEquals(null, user.getCity());
        assertEquals(null, user.getState());
        assertEquals(null, user.getPhoneNumber());
        assertEquals(false, user.getAccountCreationComplete().booleanValue());
    }

    @Test
    public void signedInTrueTest() {
        user.setSignedIn(true);
        assertEquals(true, user.getSignedIn().booleanValue());
    }

    @Test
    public void signedInFalseTest() {
        user.setSignedIn(false);
        assertEquals(false, user.getSignedIn().booleanValue());
    }

    @Test
    public void firstNameTest() {
        assertEquals("George", user.getFirstName());
    }

    @Test
    public void lastNameTest() {
        assertEquals("Kontos", user.getLastName());
    }

    @Test
    public void emailTest() {
        assertEquals("gk@gmail.com", user.getEmail());
    }

    @Test
    public void birthdayTest() {
        assertEquals("6/15/1989", user.getBirthday());
    }

    @Test
    public void cityTest() {
        assertEquals("Des Plaines", user.getCity());
    }

    @Test
    public void stateTest() {
        assertEquals("IL", user.getState());
    }

    @Test
    public void phoneTest() {
        assertEquals("847", user.getPhoneNumber());
    }

    @Test
    public void pointsTest() {
        user.setPoints(256);
        assertEquals(256, user.getPoints().intValue());
    }

    @Test
    public void accountCreationCompleteTest() {
        user.setAccountCreationComplete(true);
        assertEquals(true, user.getAccountCreationComplete().booleanValue());
    }

    @Test
    public void userNameTest() {
        assertEquals("gk", user.getUsername());
    }

    @Test
    public void rolodexTest() {
        Rolodex rolodex = new Rolodex();
        user.setRolodex(rolodex);
        assertEquals(rolodex, user.getRolodex());
    }
}
