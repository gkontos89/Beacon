package com.marshmallow.beacon.models.marketing;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
class SurveyItem {

    private String question;
    private Vector<String> options;
    private String answer;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
