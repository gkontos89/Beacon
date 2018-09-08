package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.marketing.SurveyItem;

import org.junit.Test;

import java.util.Vector;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class SurveyItemTest {

    private SurveyItem surveyItem = new SurveyItem();

    @Test
    public void questionTest() {
        surveyItem.setQuestion("What color is the sky?");
        assertEquals("What color is the sky?", surveyItem.getQuestion());
    }

    @Test
    public void optionsTest() {
        Vector<String> options = new Vector<>();
        options.add("op1");
        options.add("op2");
        surveyItem.setOptions(options);
        assertEquals(options, surveyItem.getOptions());
    }
}
