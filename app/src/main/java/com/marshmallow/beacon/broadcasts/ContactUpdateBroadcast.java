package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 7/24/2018.
 */
public class ContactUpdateBroadcast extends BaseStatusBroadcast {
    // Keys
    public static final String usernameKey = "username";
    public static final String signedInKey = "signedInKey";
    public static final String profilePictureKey = "profilePictureKey";

    // Fields
    public static final String action = "ContactUpdateBroadcast";

    public ContactUpdateBroadcast(String username, Boolean signedInStatus, String profilePicture) {
        super();
        intent.setAction(action);
        intent.putExtra(usernameKey, username);
        intent.putExtra(signedInKey, signedInStatus);
        intent.putExtra(profilePictureKey, profilePicture);
    }

    public Intent getBroadcastIntent() {
        return intent;
    }
}
