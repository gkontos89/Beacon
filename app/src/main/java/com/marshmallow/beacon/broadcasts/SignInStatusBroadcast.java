package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 5/17/2018.
 */
public class SignInStatusBroadcast extends BaseStatusBroadcast{
    // Keys
    public static final String statusMessageKey = "statusMessage";
    public static final String dataKey = "data";
    public static final String statusKey = "status";

    // Fields
    public static final String action = "SignInStatusBroadcast";
    private String statusMessage = null;
    private String data = null;

    // Statuses
    public static final String SIGN_IN_SUCCESSFUL = "SIGN_IN_SUCCESSFUL";
    public static final String SIGN_IN_FAILED = "SIGN_IN_FAILED";

    public SignInStatusBroadcast(String statusMessage, String data) {
        super();
        this.statusMessage = statusMessage;
        this.data = data;

        intent.setAction(action);
        intent.putExtra(dataKey, data);
        intent.putExtra(statusMessageKey, statusMessage);
    }

    @Override
    public Intent getSuccessfulBroadcast() {
        intent.putExtra(statusKey, SIGN_IN_SUCCESSFUL);
        return intent;
    }

    @Override
    public Intent getFailureBroadcast() {
        intent.putExtra(statusKey, SIGN_IN_FAILED);
        return intent;
    }
}
