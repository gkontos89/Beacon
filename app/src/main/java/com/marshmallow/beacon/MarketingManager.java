package com.marshmallow.beacon;

import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SurveyMarketValues;
import com.marshmallow.beacon.models.user.User;

/**
 * Created by George on 8/3/2018.
 */
public class MarketingManager {

    private static MarketingManager instance = null;

    private MarketingManager() {
    }

    public static MarketingManager getInstance() {
        if (instance == null) {
            instance = new MarketingManager();
        }

        return instance;
    }

    public Integer getUserSponsorMarketingValue(User user, SponsorMarketValues sponsorMarketValues) {
        Integer totalMarketingValue = 0;
        if (user.getFirstName().getShared()) {
            totalMarketingValue += sponsorMarketValues.getFirstNameValue();
        }

        if (user.getLastName().getShared()) {
            totalMarketingValue += sponsorMarketValues.getLastNameValue();
        }

        if (user.getEmail().getShared()) {
            totalMarketingValue += sponsorMarketValues.getEmailValue();
        }

        if (user.getBirthday().getShared()) {
            totalMarketingValue += sponsorMarketValues.getBirthdayValue();
        }

        if (user.getCity().getShared()) {
            totalMarketingValue += sponsorMarketValues.getCityValue();
        }

        if (user.getState().getShared()) {
            totalMarketingValue += sponsorMarketValues.getStateValue();
        }

        if (user.getPhoneNumber().getShared()) {
            totalMarketingValue += sponsorMarketValues.getPhoneValue();
        }

        return totalMarketingValue;
    }

    public Integer getUserSurveyMarketingValue(User user, SurveyMarketValues surveyMarketValues) {
        Integer totalMarketingValue = 0;
        if (user.getFirstName().getShared()) {
            totalMarketingValue += surveyMarketValues.getFirstNameValue();
        }

        if (user.getLastName().getShared()) {
            totalMarketingValue += surveyMarketValues.getLastNameValue();
        }

        if (user.getEmail().getShared()) {
            totalMarketingValue += surveyMarketValues.getEmailValue();
        }

        if (user.getBirthday().getShared()) {
            totalMarketingValue += surveyMarketValues.getBirthdayValue();
        }

        if (user.getCity().getShared()) {
            totalMarketingValue += surveyMarketValues.getCityValue();
        }

        if (user.getState().getShared()) {
            totalMarketingValue += surveyMarketValues.getStateValue();
        }

        if (user.getPhoneNumber().getShared()) {
            totalMarketingValue += surveyMarketValues.getPhoneValue();
        }

        return totalMarketingValue;
    }
}
