package com.marshmallow.beacon.models;

/**
 * Created by George on 7/22/2018.
 */
public class CommunityEvent {
    private Long timestamp;
    private int supplyTotal;
    private int demandTotal;

    public CommunityEvent() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSupplyTotal() {
        return supplyTotal;
    }

    public void setSupplyTotal(int supplyTotal) {
        this.supplyTotal = supplyTotal;
    }

    public int getDemandTotal() {
        return demandTotal;
    }

    public void setDemandTotal(int demandTotal) {
        this.demandTotal = demandTotal;
    }
}
