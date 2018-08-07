package com.marshmallow.beacon.models.contacts;

/**
 * Created by George on 7/17/2018.
 */
public class Request {

    private String uid;
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

    public Request(String to, String from) {
        this.to = to;
        this.from = from;
        this.status = Status.PENDING;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
