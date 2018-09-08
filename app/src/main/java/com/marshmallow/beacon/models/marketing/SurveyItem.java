package com.marshmallow.beacon.models.marketing;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveyItem {

    private String question;
    private Vector<String> options;

    public SurveyItem() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Vector<String> getOptions() {
        return options;
    }

    public void setOptions(Vector<String> options) {
        this.options = options;
    }
}
