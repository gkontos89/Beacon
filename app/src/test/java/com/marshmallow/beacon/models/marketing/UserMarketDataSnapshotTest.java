package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.user.User;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class UserMarketDataSnapshotTest {

    private UserMarketDataSnapshot userMarketDataSnapshot;

    @Before
    public void preTest() {
        User user = new User();
        user.setFirstName("George");
        user.setLastName("Kontos");
        user.setBirthday("6/15/1989");
        user.setCity("Des Plaines");
        user.setState("IL");
        user.setPhoneNumber("847");
        user.setEmail("gk@gmail.com");

        userMarketDataSnapshot = new UserMarketDataSnapshot(user);
    }

    @Test
    public void firstNameTest() {
        assertEquals("George", userMarketDataSnapshot.getFirstName());
    }

    @Test
    public void lastNameTest() {
        assertEquals("Kontos", userMarketDataSnapshot.getLastName());
    }

    @Test
    public void birthdayTest() {
        assertEquals("6/15/1989", userMarketDataSnapshot.getBirthday());
    }

    @Test
    public void emailTest() {
        assertEquals("gk@gmail.com", userMarketDataSnapshot.getEmail());
    }

    @Test
    public void cityTest() {
        assertEquals("Des Plaines", userMarketDataSnapshot.getCity());
    }

    @Test
    public void stateTest() {
        assertEquals("IL", userMarketDataSnapshot.getState());
    }

    @Test
    public void phoneTest() {
        assertEquals("847", userMarketDataSnapshot.getPhone());
    }

    @Test
    public void setFirstNameTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setFirstName("Hey");
        assertEquals("Hey", userMarketDataSnapshot.getFirstName());
    }

    @Test
    public void setLastNameTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setLastName("There");
        assertEquals("There", userMarketDataSnapshot.getLastName());
    }

    @Test
    public void setEmailTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setEmail("hey@gmail.com");
        assertEquals("hey@gmail.com", userMarketDataSnapshot.getEmail());
    }

    @Test
    public void setBirthdayTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setBirthday("123");
        assertEquals("123", userMarketDataSnapshot.getBirthday());
    }

    @Test
    public void setCityTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setCity("funtown");
        assertEquals("funtown", userMarketDataSnapshot.getCity());
    }

    @Test
    public void setStateTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setState("ID");
        assertEquals("ID", userMarketDataSnapshot.getState());
    }

    @Test
    public void setPhoneTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        userMarketDataSnapshot.setPhone("999");
        assertEquals("999", userMarketDataSnapshot.getPhone());
    }
}
