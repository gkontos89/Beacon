package com.marshmallow.beacon;

import com.marshmallow.beacon.models.marketing.SponsorMarketValues;
import com.marshmallow.beacon.models.marketing.SurveyMarketValues;
import com.marshmallow.beacon.models.user.User;

/**
 * Created by George on 8/3/2018.
 */
public class MarketingManager {

    private static MarketingManager instance = null;
    private SponsorMarketValues sponsorMarketValues;
    private SurveyMarketValues surveyMarketValues;

    private MarketingManager() {
    }

    public static MarketingManager getInstance() {
        if (instance == null) {
            instance = new MarketingManager();
        }

        return instance;
    }

    public SponsorMarketValues getSponsorMarketValues() {
        return sponsorMarketValues;
    }

    public void setSponsorMarketValues(SponsorMarketValues sponsorMarketValues) {
        this.sponsorMarketValues = sponsorMarketValues;
    }

    public SurveyMarketValues getSurveyMarketValues() {
        return surveyMarketValues;
    }

    public void setSurveyMarketValues(SurveyMarketValues surveyMarketValues) {
        this.surveyMarketValues = surveyMarketValues;
    }

    public Integer getUserSponsorMarketingValue(User user) {
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

        return totalMarketingValue;
    }

    public Integer getUserSurveyMarketingValue(User user) {
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

        return totalMarketingValue;
    }
}
