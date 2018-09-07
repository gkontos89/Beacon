package com.marshmallow.beacon.models.marketing.marketing;

import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SurveyMarketValues;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Created by George on 9/7/2018.
 */
public class SurveyMarketValuesTest {
    private SurveyMarketValues surveyMarketValues = new SurveyMarketValues();

    @Test
    public void firstNameValueTest() {
        surveyMarketValues.setFirstNameValue(5);
        assertEquals(5, surveyMarketValues.getFirstNameValue().intValue());
    }

    @Test
    public void lastNameValueTest() {
        surveyMarketValues.setLastNameValue(7);
        assertEquals(7, surveyMarketValues.getLastNameValue().intValue());
    }

    @Test
    public void geolocationValueTest() {
        surveyMarketValues.setGeolocationValue(8);
        assertEquals(8, surveyMarketValues.getGeolocationValue().intValue());
    }

    @Test
    public void emailValueTest() {
        surveyMarketValues.setEmailValue(1);
        assertEquals(1, surveyMarketValues.getEmailValue().intValue());
    }

    @Test
    public void birthdayValueTest() {
        surveyMarketValues.setBirthdayValue(22);
        assertEquals(22, surveyMarketValues.getBirthdayValue().intValue());
    }

    @Test
    public void cityValueTest() {
        surveyMarketValues.setCityValue(382);
        assertEquals(382, surveyMarketValues.getCityValue().intValue());
    }

    @Test
    public void stateValueTest() {
        surveyMarketValues.setStateValue(99);
        assertEquals(99, surveyMarketValues.getStateValue().intValue());
    }

    @Test
    public void phoneValueTest() {
        surveyMarketValues.setPhoneValue(302);
        assertEquals(302, surveyMarketValues.getPhoneValue().intValue());
    }
}

