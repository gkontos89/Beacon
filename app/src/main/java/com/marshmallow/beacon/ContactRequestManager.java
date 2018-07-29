package com.marshmallow.beacon;

import com.marshmallow.beacon.models.Request;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by George on 7/28/2018.
 */
public class ContactRequestManager {
    private static ContactRequestManager instance = null;
    private HashMap<String, Request> requestsIn;
    private HashMap<String, Request> requestsOut;

    private ContactRequestManager() {
        requestsIn = new HashMap<>();
        requestsOut = new HashMap<>();
    }

    public static ContactRequestManager getInstance() {
        if (instance == null) {
            instance = new ContactRequestManager();
        }

        return instance;
    }

    public void addRequestIn(Request requestIn) {
        requestsIn.put(requestIn.getFrom(), requestIn);
    }

    public void removeRequestIn(Request requestIn) {
        requestsIn.remove(requestIn.getFrom());
    }

    public void addRequestOut(Request requestOut) {
        requestsOut.put(requestOut.getTo(), requestOut);
    }

    public void removeRequestsOut(Request requestOut) {
        requestsOut.remove(requestOut.getTo());
    }


    public Vector<Request> getIncomingRequests() {
        return new Vector<>(requestsIn.values());
    }

    public Vector<Request> getOutgoingRequests() {
        return new Vector<>(requestsOut.values());
    }
}
