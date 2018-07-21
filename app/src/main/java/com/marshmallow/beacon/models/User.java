package com.marshmallow.beacon.models;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/17/2018.
 */
public class User {

    private String username;
    private Boolean demandStatus;
    private Boolean supplyStatus;
    private Rolodex rolodex;
    private List<Request> requestsIn;
    private List<Request> requestsOut;

    public User () {
    }

    public User(String username) {
        this.username = username;
        demandStatus = false;
        supplyStatus = false;
        rolodex = new Rolodex();
        requestsIn = new Vector<>();
        requestsOut = new Vector<>();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Boolean getDemandStatus() { return demandStatus; }

    public void setDemandStatus(Boolean demandStatus) {
        this.demandStatus = demandStatus;

    }

    public Boolean getSupplyStatus() { return supplyStatus; }

    public void setSupplyStatus(Boolean supplyStatus) {
        this.supplyStatus = supplyStatus;
    }

    public Rolodex getRolodex() { return rolodex; }
    public void setRolodex(Rolodex rolodex) { this.rolodex = rolodex; }
    public List<Request> getRequestsIn() { return requestsIn; }
    public void setRequestsIn(List<Request> requestsIn) { this.requestsIn = requestsIn; }
    public List<Request> getRequestsOut() { return requestsOut; }
    public void setRequestsOut(List<Request> requestsOut) { this.requestsOut = requestsOut; }

    public void acceptRequest(String requestUid) {

    }

    public void declineRequest(String requestUid) {

    }

    public void confirmRequestAcceptance(String requestUid) {

    }

}
