package com.marshmallow.beacon.models.marketing;

/**
 * Created by George on 9/15/2018.
 */
public class DistributedSurvey {
    private String surveyUid;
    private String sponsorUid;

    public DistributedSurvey() {
    }

    public String getSurveyUid() {
        return surveyUid;
    }

    public void setSurveyUid(String surveyUid) {
        this.surveyUid = surveyUid;
    }

    public String getSponsorUid() {
        return sponsorUid;
    }

    public void setSponsorUid(String sponsorUid) {
        this.sponsorUid = sponsorUid;
    }
}
