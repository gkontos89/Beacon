package com.marshmallow.beacon.broadcasts;

import android.content.Intent;

/**
 * Created by George on 7/28/2018.
 */
public class AddNewContactBroadcast extends BaseStatusBroadcast {

    // Keys
    public static final String statusMessageKey = "statusMessage";
    public static final String dataKey = "data";
    public static final String statusKey = "status";

    // Fields
    public static final String action = "AddNewContactBroadcast";
    private String statusMessage = null;
    private String data = null;

    // Statuses
    public static final String CONTACT_REQUEST_SUBMITTED = "CONTACT_REQUEST_SUBMITTED";
    public static final String CONTACT_NOT_FOUND = "CONTACT_NOT_FOUND";
    public static final String CONTACT_REQUEST_FAILED = "CONTACT_REQUEST_FAILED";

    public AddNewContactBroadcast(String statusMessage, String data) {
        super();
        this.statusMessage = statusMessage;
        this.data = data;

        intent.setAction(action);
        intent.putExtra(dataKey, data);
        intent.putExtra(statusMessageKey, statusMessage);
    }

    @Override
    public Intent getSuccessfulBroadcast() {
        intent.putExtra(statusKey, CONTACT_REQUEST_SUBMITTED);
        return intent;
    }

    @Override
    public Intent getFailureBroadcast() {
        intent.putExtra(statusKey, CONTACT_REQUEST_FAILED);
        return intent;
    }
}
