package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 6/1/2018.
 */
public interface StatusBroadcast {
    /**
     *
     * @return intent to broadcast on success
     */
    Intent getSuccessfulBroadcast();

    /**
     *
     * @return intent to broadcast on failure
     */
    Intent getFailureBroadcast();
}
