package com.marshmallow.beacon.models.marketing;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class Survey {
    private List<SurveyItem> surveyItems;
    private int pointReward;
    private String surveyTitle;
    private String surveyUid;
    private String sponsorUid;

    public Survey() {
    }

    public List<SurveyItem> getSurveyItems() {
        return surveyItems;
    }

    public void setSurveyItems(List<SurveyItem> surveyItems) {
        this.surveyItems = surveyItems;
    }

    public int getPointReward() {
        return pointReward;
    }

    public void setPointReward(int pointReward) {
        this.pointReward = pointReward;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
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
