package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 7/29/2018.
 */
public class RequestUpdateBroadcast extends BaseStatusBroadcast{

    // Fields
    public static final String action = "RequestUpdateBroadcast";

    public RequestUpdateBroadcast() {
        super();
        intent.setAction(action);
    }

    public Intent getRequestUpdateBroadcast() {
        return intent;
    }
}
