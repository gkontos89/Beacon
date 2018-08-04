package com.marshmallow.beacon;

import com.marshmallow.beacon.models.SponsorMarketingInfo;
import com.marshmallow.beacon.models.SurveyMarketingInfo;
import com.marshmallow.beacon.models.User;

/**
 * Created by George on 8/3/2018.
 */
public class MarketingManager {

    private static MarketingManager instance = null;
    private SponsorMarketingInfo sponsorMarketingInfo;
    private SurveyMarketingInfo surveyMarketingInfo;

    private MarketingManager() {
    }

    public static MarketingManager getInstance() {
        if (instance == null) {
            instance = new MarketingManager();
        }

        return instance;
    }

    public SponsorMarketingInfo getSponsorMarketingInfo() {
        return sponsorMarketingInfo;
    }

    public void setSponsorMarketingInfo(SponsorMarketingInfo sponsorMarketingInfo) {
        this.sponsorMarketingInfo = sponsorMarketingInfo;
    }

    public SurveyMarketingInfo getSurveyMarketingInfo() {
        return surveyMarketingInfo;
    }

    public void setSurveyMarketingInfo(SurveyMarketingInfo surveyMarketingInfo) {
        this.surveyMarketingInfo = surveyMarketingInfo;
    }

    public Integer getUserSponsorMarketingValue(User user) {
        Integer totalMarketingValue = 0;
        if (user.getFirstName().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getFirstNameValue();
        }

        if (user.getLastName().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getLastNameValue();
        }

        if (user.getEmail().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getEmailValue();
        }

        if (user.getBirthday().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getBirthdayValue();
        }

        if (user.getCity().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getCityValue();
        }

        if (user.getState().getShared()) {
            totalMarketingValue += sponsorMarketingInfo.getStateValue();
        }

        return totalMarketingValue;
    }

    public Integer getUserSurveyMarketingValue(User user) {
        Integer totalMarketingValue = 0;
        if (user.getFirstName().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getFirstNameValue();
        }

        if (user.getLastName().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getLastNameValue();
        }

        if (user.getEmail().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getEmailValue();
        }

        if (user.getBirthday().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getBirthdayValue();
        }

        if (user.getCity().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getCityValue();
        }

        if (user.getState().getShared()) {
            totalMarketingValue += surveyMarketingInfo.getStateValue();
        }

        return totalMarketingValue;
    }
}
