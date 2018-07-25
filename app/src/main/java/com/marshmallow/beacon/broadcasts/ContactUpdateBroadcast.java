package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 7/24/2018.
 */
public class ContactUpdateBroadcast extends BaseStatusBroadcast {
    // Keys
    public static final String usernameKey = "username";
    public static final String demandStatusKey = "demandStatus";
    public static final String supplyStatusKey = "supplyStatus";

    // Fields
    public static final String action = "ContactUpdateBroadcast";

    public ContactUpdateBroadcast(String username, Boolean demandStatus, Boolean supplyStatus) {
        super();
        intent.setAction(action);
        intent.putExtra(usernameKey, username);
        intent.putExtra(demandStatusKey, demandStatus);
        intent.putExtra(supplyStatusKey, supplyStatus);
    }

    public Intent getBroadcastIntent() {
        return intent;
    }
}
