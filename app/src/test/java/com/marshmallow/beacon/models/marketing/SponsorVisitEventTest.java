package com.marshmallow.beacon.models.marketing.marketing;

import com.marshmallow.beacon.models.marketing.SponsorVisitEvent;
import com.marshmallow.beacon.models.marketing.UserMarketDataSnapshot;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by George on 9/7/2018.
 */
public class SponsorVisitEventTest {

    @Test
    public void creationTest() {
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent(userMarketDataSnapshot);
        assertEquals(userMarketDataSnapshot, sponsorVisitEvent.getUserMarketDataSnapshot());
    }

    @Test
    public void userMarketDataSnapshotTest() {
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent();
        UserMarketDataSnapshot userMarketDataSnapshot = new UserMarketDataSnapshot();
        sponsorVisitEvent.setUserMarketDataSnapshot(userMarketDataSnapshot);
        assertEquals(userMarketDataSnapshot, sponsorVisitEvent.getUserMarketDataSnapshot());
    }

    @Test
    public void timestampTest() {
        SponsorVisitEvent sponsorVisitEvent = new SponsorVisitEvent();
        long timestamp = System.currentTimeMillis();
        sponsorVisitEvent.setTimestamp(timestamp);
        assertEquals(timestamp, sponsorVisitEvent.getTimestamp().longValue());
    }

}
