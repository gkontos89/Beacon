package com.marshmallow.beacon.models.marketing.marketing;

import android.os.SystemClock;

import com.marshmallow.beacon.models.marketing.SurveyResponseItem;
import com.marshmallow.beacon.models.marketing.SurveyResult;
import com.marshmallow.beacon.models.marketing.UserMarketDataSnapshot;

import org.junit.Test;

import java.util.Vector;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class SurveyResultTest {

    private SurveyResult surveyResult = new SurveyResult();

    @Test
    public void timestampTest() {
        long timestamp = System.currentTimeMillis();
        surveyResult.setTimestamp(timestamp);
        assertEquals(timestamp, surveyResult.getTimestamp().longValue());
    }

    @Test
    public void surveyResponseItemTest() {
        Vector<SurveyResponseItem> surveyResponseItems = new Vector<>();
        surveyResult.setSurveyResponseItems(surveyResponseItems);
        assertEquals(surveyResponseItems, surveyResult.getSurveyResponseItems());
    }

    @Test
    public void userMarketDataSnapshot() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        surveyResult.setUserMarketDataSnapshot(userMarketDataSnapshot);
        assertEquals(userMarketDataSnapshot, surveyResult.getUserMarketDataSnapshot());
    }
}
