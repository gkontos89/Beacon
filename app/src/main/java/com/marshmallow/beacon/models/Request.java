package com.marshmallow.beacon.models;

import java.util.Enumeration;

/**
 * Created by George on 7/17/2018.
 */
public class Request {

    private String to;
    private String from;
    private Status status;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public Request() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
