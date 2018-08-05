package com.marshmallow.beacon.models.user;

/**
 * Created by George on 8/3/2018.
 */
public class DataPoint {
    private String value;
    private Boolean shared;

    public DataPoint() {
    }

    public DataPoint(String value, Boolean shared) {
        this.value = value;
        this.shared = shared;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }
}
