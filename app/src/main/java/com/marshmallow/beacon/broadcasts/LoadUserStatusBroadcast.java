package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 6/1/2018.
 */
public class LoadUserStatusBroadcast extends BaseStatusBroadcast{

    // Keys
    public static final String statusMessageKey = "statusMessage";
    public static final String dataKey = "data";
    public static final String statusKey = "status";

    // Fields
    public static final String action = "LoaderUserStatusBroadcast";
    private final String statusMessage;
    private final String data;

    // Statuses
    public static final String USER_LOADED_SUCCESSFUL = "USER_LOAD_SUCCESSFUL";
    public static final String USER_LOADED_FAILED = "USER_LOAD_FAILED";

    public LoadUserStatusBroadcast(String statusMessage, String data) {
        super();
        this.statusMessage = statusMessage;
        this.data = data;

        intent.setAction(action);
        intent.putExtra(dataKey, data);
        intent.putExtra(statusMessageKey, statusMessage);
    }

    @Override
    public Intent getSuccessfulBroadcast() {
        intent.putExtra(statusKey, USER_LOADED_SUCCESSFUL);
        return intent;
    }

    @Override
    public Intent getFailureBroadcast() {
        intent.putExtra(statusKey, USER_LOADED_FAILED);
        return intent;
    }
}
