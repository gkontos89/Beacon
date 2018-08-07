package com.marshmallow.beacon.models.marketing;

import java.util.Vector;

/**
 * Created by George on 8/5/2018.
 */
public class SurveyResponseItem {

    private String question;
    private String answer;

    public SurveyResponseItem() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
