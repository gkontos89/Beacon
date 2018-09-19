package com.marshmallow.beacon;

import android.os.SystemClock;

import com.marshmallow.beacon.models.marketing.Survey;
import com.marshmallow.beacon.models.marketing.SurveyItem;
import com.marshmallow.beacon.models.marketing.SurveyResponseItem;
import com.marshmallow.beacon.models.marketing.SurveyResult;
import com.marshmallow.beacon.models.marketing.UserMarketDataSnapshot;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by George on 9/15/2018.
 */
public class SurveyManager {
    private static SurveyManager instance = null;
    private Survey survey;
    private HashMap<SurveyItem, SurveyResponseItem> surveyResponseItems;

    private SurveyManager() {
    }

    public static SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
        }

        return instance;
    }

    public void setCurrentSurvey(Survey survey) {
        this.survey = survey;
        for (SurveyItem surveyItem : this.survey.getSurveyItems()) {
            SurveyResponseItem surveyResponseItem = new SurveyResponseItem();
            surveyResponseItem.setQuestion(surveyItem.getQuestion());
            surveyResponseItem.setAnswer(null);
            surveyResponseItems.put(surveyItem, surveyResponseItem);
        }
    }

    public Survey getCurrentSurvey() {
        return survey;
    }

    public void setSurveyResponseItemAnswer(SurveyItem surveyItem, String answer) {
        surveyResponseItems.get(surveyItem).setAnswer(answer);
    }

    public SurveyResult generateSurveyResult() {
        long timestamp = System.currentTimeMillis();
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot(UserManager.getInstance().getUser());

        SurveyResult surveyResult = new SurveyResult();
        surveyResult.setTimestamp(timestamp);
        surveyResult.setUserMarketDataSnapshot(userMarketDataSnapshot);
        surveyResult.setSurveyResponseItems((Vector<SurveyResponseItem>) surveyResponseItems.values());
        return surveyResult;
    }
}
