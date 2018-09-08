package com.marshmallow.beacon.models.user;

import com.marshmallow.beacon.models.user.DataPoint;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by George on 9/7/2018.
 */
public class DataPointTest {

    private DataPoint dataPoint = new DataPoint();

    @Test
    public void creation() {
        DataPoint dataPoint = new DataPoint("hello", false);
        assertEquals(false, dataPoint.getShared().booleanValue());
        assertEquals("hello", dataPoint.getValue());
    }

    @Test
    public void sharedTrueTest() {
        dataPoint.setShared(true);
        assertEquals(true, dataPoint.getShared().booleanValue());
    }

    @Test
    public void sharedFalseTest() {
        dataPoint.setShared(false);
        assertEquals(false, dataPoint.getShared().booleanValue());
    }

    @Test
    public void valueTest() {
        dataPoint.setValue("hello");
        assertEquals("hello", dataPoint.getValue());
    }
}
