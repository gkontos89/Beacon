package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.marketing.SponsorMarketValues;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Created by George on 9/7/2018.
 */
public class SponsorMarketValuesTest {
    private SponsorMarketValues sponsorMarketValues = new SponsorMarketValues();

    @Test
    public void firstNameValueTest() {
        sponsorMarketValues.setFirstNameValue(5);
        assertEquals(5, sponsorMarketValues.getFirstNameValue().intValue());
    }

    @Test
    public void lastNameValueTest() {
        sponsorMarketValues.setLastNameValue(7);
        assertEquals(7, sponsorMarketValues.getLastNameValue().intValue());
    }

    @Test
    public void geolocationValueTest() {
        sponsorMarketValues.setGeolocationValue(8);
        assertEquals(8, sponsorMarketValues.getGeolocationValue().intValue());
    }

    @Test
    public void emailValueTest() {
        sponsorMarketValues.setEmailValue(1);
        assertEquals(1, sponsorMarketValues.getEmailValue().intValue());
    }

    @Test
    public void birthdayValueTest() {
        sponsorMarketValues.setBirthdayValue(22);
        assertEquals(22, sponsorMarketValues.getBirthdayValue().intValue());
    }

    @Test
    public void cityValueTest() {
        sponsorMarketValues.setCityValue(382);
        assertEquals(382, sponsorMarketValues.getCityValue().intValue());
    }

    @Test
    public void stateValueTest() {
        sponsorMarketValues.setStateValue(99);
        assertEquals(99, sponsorMarketValues.getStateValue().intValue());
    }

    @Test
    public void phoneValueTest() {
        sponsorMarketValues.setPhoneValue(302);
        assertEquals(302, sponsorMarketValues.getPhoneValue().intValue());
    }
}

