package com.marshmallow.beacon.models;

import com.marshmallow.beacon.MarketingManager;
import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SurveyMarketValues;
import com.marshmallow.beacon.models.user.DataPoint;
import com.marshmallow.beacon.models.user.User;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class MarketingManagerTest {

    private User user = new User();
    private SponsorMarketValues sponsorMarketValues = new SponsorMarketValues();
    private SurveyMarketValues surveyMarketValues = new SurveyMarketValues();

    @Before
    public void preTest() {
        user.setFirstName(new DataPoint("", true));
        user.setLastName(new DataPoint("", true));
        user.setEmail(new DataPoint("", true));
        user.setPhoneNumber(new DataPoint("", true));
        user.setCity(new DataPoint("", true));
        user.setState(new DataPoint("", true));
        user.setBirthday(new DataPoint("", true));

        sponsorMarketValues.setPhoneValue(5);
        sponsorMarketValues.setStateValue(1);
        sponsorMarketValues.setCityValue(3);
        sponsorMarketValues.setBirthdayValue(9);
        sponsorMarketValues.setEmailValue(2);
        sponsorMarketValues.setLastNameValue(1);
        sponsorMarketValues.setFirstNameValue(88);

        surveyMarketValues.setPhoneValue(5);
        surveyMarketValues.setStateValue(1);
        surveyMarketValues.setCityValue(3);
        surveyMarketValues.setBirthdayValue(9);
        surveyMarketValues.setEmailValue(2);
        surveyMarketValues.setLastNameValue(1);
        surveyMarketValues.setFirstNameValue(88);
    }

    @Test
    public void singletonTest() {
        MarketingManager marketingManager1 = MarketingManager.getInstance();
        MarketingManager marketingManager2 = MarketingManager.getInstance();
        assertEquals(marketingManager1, marketingManager2);
    }

    @Test
    public void sponsorMarketValueTest() {
        assertEquals(109, MarketingManager.getInstance().getUserSponsorMarketingValue(user, sponsorMarketValues).intValue());
    }

    @Test
    public void surveyMarketValueTest() {
        assertEquals(109, MarketingManager.getInstance().getUserSurveyMarketingValue(user, surveyMarketValues).intValue());
    }


}
