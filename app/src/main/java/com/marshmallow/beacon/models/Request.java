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
        status = Status.PENDING;
    }
}
