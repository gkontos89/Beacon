package com.marshmallow.beacon.models.marketing;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class Survey {
    private Vector<SurveyItem> surveyItems;

    public Survey() {
    }

    public Vector<SurveyItem> getSurveyItems() {
        return surveyItems;
    }

    public void setSurveyItems(Vector<SurveyItem> surveyItems) {
        this.surveyItems = surveyItems;
    }
}
