package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 6/1/2018.
 */
public class BaseStatusBroadcast implements StatusBroadcast {
    // Returned Intent
    protected Intent intent;
    
    protected BaseStatusBroadcast() {
        intent = new Intent();
    }

    @Override
    public Intent getSuccessfulBroadcast() {
        return null;
    }

    @Override
    public Intent getFailureBroadcast() {
        return null;
    }
}
