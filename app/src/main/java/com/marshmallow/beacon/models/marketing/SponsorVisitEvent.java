package com.marshmallow.beacon.models.marketing;

/**
 * Created by George on 8/5/2018.
 */
public class SponsorVisitEvent {

    private UserMarketDataSnapshot userMarketDataSnapshot;
    private Long timestamp;

    public SponsorVisitEvent() {
    }

    public SponsorVisitEvent(UserMarketDataSnapshot userMarketDataSnapshot) {
        this.userMarketDataSnapshot = userMarketDataSnapshot;
        timestamp = System.currentTimeMillis();
    }

    public UserMarketDataSnapshot getUserMarketDataSnapshot() {
        return userMarketDataSnapshot;
    }

    public void setUserMarketDataSnapshot(UserMarketDataSnapshot userMarketDataSnapshot) {
        this.userMarketDataSnapshot = userMarketDataSnapshot;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
