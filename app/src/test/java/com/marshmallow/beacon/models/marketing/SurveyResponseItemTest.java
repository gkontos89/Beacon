package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.marketing.SurveyResponseItem;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class SurveyResponseItemTest {

    private SurveyResponseItem surveyResponseItem = new SurveyResponseItem();

    @Test
    public void questionTest() {
        surveyResponseItem.setQuestion("What's your favorite color?");
        assertEquals("What's your favorite color?", surveyResponseItem.getQuestion());
    }

    @Test
    public void answerTest() {
        surveyResponseItem.setAnswer("Blue");
        assertEquals("Blue", surveyResponseItem.getAnswer());
    }
}
