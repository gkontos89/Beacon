package com.marshmallow.beacon.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.broadcasts.RequestUpdateBroadcast;
import com.marshmallow.beacon.models.Request;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
public class OutgoingContactRequestsActivity extends AppCompatActivity {

    // GUI handles
    private RecyclerView outgoingRequestsRecyclerView;
    private RecyclerView.LayoutManager outgoingRequestsLayoutManager;
    private OutgoingContactRequestsAdapter outgoingContactRequestsAdapter;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private Vector<Request> outgoingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_contact_requests);

        // GUI handle instantiation
        outgoingRequestsRecyclerView = findViewById(R.id.outgoing_request_recycler_view);
        outgoingRequestsLayoutManager = new LinearLayoutManager(this);
        outgoingRequestsRecyclerView.setLayoutManager(outgoingRequestsLayoutManager);
        outgoingRequests = ContactRequestManager.getInstance().getOutgoingRequests();
        outgoingContactRequestsAdapter = new OutgoingContactRequestsAdapter(outgoingRequests);
        outgoingRequestsRecyclerView.setAdapter(outgoingContactRequestsAdapter);

        // Initialize broadcastReceiver and its filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(RequestUpdateBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                outgoingRequests = ContactRequestManager.getInstance().getOutgoingRequests();
                outgoingContactRequestsAdapter.notifyDataSetChanged();
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
