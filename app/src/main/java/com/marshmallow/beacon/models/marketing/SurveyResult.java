package com.marshmallow.beacon.models.marketing;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveyResult {

    private Vector<SurveyResponseItem> surveyResponseItems;
    private UserMarketDataSnapshot userMarketDataSnapshot;
    private Long timestamp;

    public SurveyResult() {
    }

    public Vector<SurveyResponseItem> getSurveyResponseItems() {
        return surveyResponseItems;
    }

    public void setSurveyResponseItems(Vector<SurveyResponseItem> surveyResponseItems) {
        this.surveyResponseItems = surveyResponseItems;
    }

    public UserMarketDataSnapshot getUserMarketDataSnapshot() {
        return userMarketDataSnapshot;
    }

    public void setUserMarketDataSnapshot(UserMarketDataSnapshot userMarketDataSnapshot) {
        this.userMarketDataSnapshot = userMarketDataSnapshot;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
