package com.marshmallow.beacon.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.broadcasts.RequestUpdateBroadcast;

/**
 * Created by George on 7/13/2018.
 */
public class HomeActivity extends BaseActivity {

    // GUI handles
    TextView usernameText;
    CardView incomingRequestsCard;
    TextView incomingRequestsCountText;
    CardView outgoingRequestsCard;
    TextView outgoingRequestsCountText;
    BeaconButton supplyButton;
    TextView supplyStatusText;
    BeaconButton demandButton;
    TextView demandStatusText;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        usernameText = findViewById(R.id.username_title);
        incomingRequestsCard = findViewById(R.id.incoming_requests);
        incomingRequestsCountText = findViewById(R.id.received_contact_request_count);
        outgoingRequestsCard = findViewById(R.id.outgoing_requests);
        outgoingRequestsCountText = findViewById(R.id.sent_contact_request_count);
        supplyButton = findViewById(R.id.supply_button);
        supplyStatusText = findViewById(R.id.supply_status_text);
        demandButton = findViewById(R.id.demand_button);
        demandStatusText = findViewById(R.id.demand_status_text);

        // UI initialization
        usernameText.setText(UserManager.getInstance().getUser().getUsername());
        updateRequestCounts();
        supplyButton.initialize(UserManager.getInstance().getUser().getSupplyStatus(), supplyStatusText);
        demandButton.initialize(UserManager.getInstance().getUser().getDemandStatus(), demandStatusText);

        incomingRequestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IncomingContactRequestsActivity.class);
                startActivity(intent);
            }
        });

        outgoingRequestsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OutgoingContactRequestsActivity.class);
                startActivity(intent);
            }
        });

        // Setup broadcast receivers for contact request updates
        intentFilter = new IntentFilter();
        intentFilter.addAction(RequestUpdateBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateRequestCounts();
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeaconBackend.getInstance().signOutUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRequestCounts();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void updateRequestCounts() {
        incomingRequestsCountText.setText(String.format("%d", ContactRequestManager.getInstance().getIncomingRequests().size()));
        outgoingRequestsCountText.setText(String.format("%d", ContactRequestManager.getInstance().getOutgoingRequests().size()));
    }
}
