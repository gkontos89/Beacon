package com.marshmallow.beacon.ui.contacts;

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
import com.marshmallow.beacon.models.contacts.Request;

import java.util.Vector;

/**
 * Created by George on 7/29/2018.
 */
public class IncomingContactRequestsActivity extends AppCompatActivity {

    // GUI handles
    private RecyclerView incomingRequestsRecyclerView;
    private RecyclerView.LayoutManager incomingRequestsLayoutManager;
    private IncomingContactRequestsAdapter incomingContactRequestsAdapter;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private Vector<Request> incomingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_contact_requests);

        // GUI handle instantiation
        incomingRequestsRecyclerView = findViewById(R.id.incoming_requests_recycler_view);
        incomingRequestsLayoutManager = new LinearLayoutManager(this);
        incomingRequestsRecyclerView.setLayoutManager(incomingRequestsLayoutManager);
        incomingRequests = ContactRequestManager.getInstance().getIncomingRequests();
        incomingContactRequestsAdapter = new IncomingContactRequestsAdapter(incomingRequests);
        incomingRequestsRecyclerView.setAdapter(incomingContactRequestsAdapter);

        // Initialize broadcastReceiver and its filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(RequestUpdateBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                incomingRequests = ContactRequestManager.getInstance().getIncomingRequests();
                incomingContactRequestsAdapter.setIncomingRequestReference(incomingRequests);
                incomingContactRequestsAdapter.notifyDataSetChanged();
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
