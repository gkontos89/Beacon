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

    public User () {
    }

    public User(String username) {
        this.username = username;
        demandStatus = false;
        supplyStatus = false;
        rolodex = new Rolodex();
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
}
