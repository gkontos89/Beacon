package com.marshmallow.beacon.models;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/17/2018.
 */
public class Rolodex {

    private List<String> uids;

    public Rolodex() {
        uids = new Vector<>();
    }

    public List<String> getUids() { return uids; }
    public void removeUid(String uid) { uids.remove(uid); }
    public void addUid(String uid) { uids.add(uid); }
}
