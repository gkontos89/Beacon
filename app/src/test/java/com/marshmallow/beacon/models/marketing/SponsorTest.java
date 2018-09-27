package com.marshmallow.beacon.models.marketing;

import com.marshmallow.beacon.models.marketing.Sponsor;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by George on 9/7/2018.
 */
public class SponsorTest {
    private Sponsor sponsor = new Sponsor();

    @Test
    public void nameTest() {
        sponsor.setName("sponsor");
        assertEquals("sponsor", sponsor.getName());
    }

    @Test
    public void uidTest() {
        sponsor.setUid("12345");
        assertEquals("12345", sponsor.getUid());
    }

    @Test
    public void pictureTest() {
        sponsor.setPicture("alfihwefih");
        assertEquals("alfihwefih", sponsor.getPicture());
    }

    @Test
    public void urlTest() {
        sponsor.setUrl("hello.com");
        assertEquals("hello.com", sponsor.getUrl());
    }

    @Test
    public void pictureIsNullTest() {
        sponsor.setPicture(null);
        assertEquals(null, sponsor.getProfilePictureBitmap());
    }
}

