package com.marshmallow.beacon.models;

/**
 * Created by George on 7/22/2018.
 */
public class UserEvent {
    private int timestamp;
    private String userUniqueId;
    private Boolean supplyStatus;
    private Boolean demandStatus;

    public UserEvent() {
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public Boolean getSupplyStatus() {
        return supplyStatus;
    }

    public void setSupplyStatus(Boolean supplyStatus) {
        this.supplyStatus = supplyStatus;
    }

    public Boolean getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(Boolean demandStatus) {
        this.demandStatus = demandStatus;
    }
}
