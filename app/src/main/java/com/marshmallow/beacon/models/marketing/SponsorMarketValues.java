package com.marshmallow.beacon.models.marketing;

/**
 * Created by George on 8/3/2018.
 */
public class SponsorMarketValues {

    private Integer firstNameValue;
    private Integer lastNameValue;
    private Integer emailValue;
    private Integer birthdayValue;
    private Integer cityValue;
    private Integer stateValue;

    public SponsorMarketValues() {
        // TODO remove
        firstNameValue = 1;
        lastNameValue = 1;
        emailValue = 1;
        birthdayValue = 1;
        cityValue = 1;
        stateValue = 1;
    }

    public Integer getFirstNameValue() {
        return firstNameValue;
    }

    public void setFirstNameValue(Integer firstNameValue) {
        this.firstNameValue = firstNameValue;
    }

    public Integer getLastNameValue() {
        return lastNameValue;
    }

    public void setLastNameValue(Integer lastNameValue) {
        this.lastNameValue = lastNameValue;
    }

    public Integer getEmailValue() {
        return emailValue;
    }

    public void setEmailValue(Integer emailValue) {
        this.emailValue = emailValue;
    }

    public Integer getBirthdayValue() {
        return birthdayValue;
    }

    public void setBirthdayValue(Integer birthdayValue) {
        this.birthdayValue = birthdayValue;
    }

    public Integer getCityValue() {
        return cityValue;
    }

    public void setCityValue(Integer cityValue) {
        this.cityValue = cityValue;
    }

    public Integer getStateValue() {
        return stateValue;
    }

    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }
}
