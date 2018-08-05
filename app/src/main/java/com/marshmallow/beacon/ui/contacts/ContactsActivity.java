package com.marshmallow.beacon.ui.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.marshmallow.beacon.ContactRequestManager;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.broadcasts.ContactUpdateBroadcast;
import com.marshmallow.beacon.broadcasts.RequestUpdateBroadcast;
import com.marshmallow.beacon.models.contacts.Contact;
import com.marshmallow.beacon.ui.BaseActivity;

import java.util.Vector;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsActivity extends BaseActivity {

    // GUI handles
    private CardView incomingRequestsCard;
    private TextView incomingRequestsCountText;
    private CardView outgoingRequestsCard;
    private TextView outgoingRequestsCountText;
    private TextView noContactsText;
    private RecyclerView contactsRecyclerView;
    private FloatingActionButton addContactButton;
    private RecyclerView.LayoutManager contactsLayoutManager;
    private ContactsAdapter contactsAdapter;
    private Vector<String> contactUsernames;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        incomingRequestsCard = findViewById(R.id.incoming_requests);
        incomingRequestsCountText = findViewById(R.id.received_contact_request_count);
        outgoingRequestsCard = findViewById(R.id.outgoing_requests);
        outgoingRequestsCountText = findViewById(R.id.sent_contact_request_count);
        noContactsText = findViewById(R.id.no_contacts_title);
        contactUsernames = new Vector<>();
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
        contactsAdapter = new ContactsAdapter(this, contactUsernames);
        contactsRecyclerView.setAdapter(contactsAdapter);
        addContactButton = findViewById(R.id.add_contact_fab);

        // UI initialization
        updateNumberOfContacts();
        updateRequestCounts();

        // Set GUI business logic
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewContactActivity.class);
                startActivity(intent);
            }
        });

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

        // Initialize broadcastReceiver and its filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(ContactUpdateBroadcast.action);
        intentFilter.addAction(RequestUpdateBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case ContactUpdateBroadcast.action:
                            Contact contact = new Contact();
                            contact.setUsername(intent.getStringExtra(ContactUpdateBroadcast.usernameKey));
                            contact.setSignedIn(intent.getBooleanExtra(ContactUpdateBroadcast.signedInKey, false));
                            contact.setProfilePicture(intent.getStringExtra(ContactUpdateBroadcast.profilePictureKey));
                            UserManager.getInstance().getContacts().put(contact.getUsername(), contact);
                            if (!contactUsernames.contains(contact.getUsername())) {
                                contactUsernames.add(contact.getUsername());
                            }

                            contactsAdapter.notifyDataSetChanged();
                            updateNumberOfContacts();
                            break;

                        case RequestUpdateBroadcast.action:
                            updateRequestCounts();
                    }
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);

        // Finally set up contact listeners
        BeaconBackend.getInstance().initializeContactListeners(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeaconBackend.getInstance().removeContactListeners();
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        BeaconBackend.getInstance().removeContactListeners();
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

    private void updateNumberOfContacts() {
        if (contactUsernames.size() != 0) {
            noContactsText.setVisibility(View.GONE);
            contactsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noContactsText.setVisibility(View.VISIBLE);
            contactsRecyclerView.setVisibility(View.GONE);
        }
    }
}
