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
        user.setFirstName(new DataPoint("George", true));
        user.setLastName(new DataPoint("Kontos", true));
        user.setBirthday(new DataPoint("6/15/1989", true));
        user.setCity(new DataPoint("Des Plaines", true));
        user.setState(new DataPoint("IL", true));
        user.setPhoneNumber(new DataPoint("847", true));
        user.setEmail(new DataPoint("gk@gmail.com", true));
    }

    @Test
    public void creationTest() {
        User user = new User("gk@gmail.com");
        assertEquals(null, user.getUsername());
        assertEquals(null, user.getProfilePicture());
        assertEquals(true, user.getSignedIn().booleanValue());
        assertEquals(0, user.getPoints().intValue());
        assertEquals(null, user.getFirstName().getValue());
        assertEquals(false, user.getFirstName().getShared().booleanValue());
        assertEquals(null, user.getLastName().getValue());
        assertEquals(false, user.getLastName().getShared().booleanValue());
        assertEquals("gk@gmail.com", user.getEmail().getValue());
        assertEquals(true, user.getEmail().getShared().booleanValue());
        assertEquals(null, user.getBirthday().getValue());
        assertEquals(false, user.getBirthday().getShared().booleanValue());
        assertEquals(null, user.getCity().getValue());
        assertEquals(false, user.getCity().getShared().booleanValue());
        assertEquals(null, user.getState().getValue());
        assertEquals(false, user.getState().getShared().booleanValue());
        assertEquals(null, user.getPhoneNumber().getValue());
        assertEquals(false, user.getPhoneNumber().getShared().booleanValue());
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
        assertEquals("George", user.getFirstName().getValue());
    }

    @Test
    public void lastNameTest() {
        assertEquals("Kontos", user.getLastName().getValue());
    }

    @Test
    public void emailTest() {
        assertEquals("gk@gmail.com", user.getEmail().getValue());
    }

    @Test
    public void birthdayTest() {
        assertEquals("6/15/1989", user.getBirthday().getValue());
    }

    @Test
    public void cityTest() {
        assertEquals("Des Plaines", user.getCity().getValue());
    }

    @Test
    public void stateTest() {
        assertEquals("IL", user.getState().getValue());
    }

    @Test
    public void phoneTest() {
        assertEquals("847", user.getPhoneNumber().getValue());
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
