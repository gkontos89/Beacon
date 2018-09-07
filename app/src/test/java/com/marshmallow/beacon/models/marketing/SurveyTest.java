package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.marketing.Survey;
import com.marshmallow.beacon.models.marketing.SurveyItem;

import org.junit.Test;

import java.util.Vector;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class SurveyTest {
    private Survey survey = new Survey();

    @Test
    public void getSurveyItemsTest() {
        Vector<SurveyItem> surveyItems = new Vector<>();
        survey.setSurveyItems(surveyItems);
        assertEquals(surveyItems, survey.getSurveyItems());
    }
}
