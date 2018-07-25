package com.marshmallow.beacon.models;

/**
 * Created by George on 7/24/2018.
 */
public class Contact {
    private String username;
    private Boolean demandStatus;
    private Boolean supplyStatus;

    public Contact() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(Boolean demandStatus) {
        this.demandStatus = demandStatus;
    }

    public Boolean getSupplyStatus() {
        return supplyStatus;
    }

    public void setSupplyStatus(Boolean supplyStatus) {
        this.supplyStatus = supplyStatus;
    }
}
