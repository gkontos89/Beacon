package com.marshmallow.beacon.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.SnapshotParser;
import com.marshmallow.beacon.R;
import com.marshmallow.beacon.UserManager;
import com.marshmallow.beacon.backend.BeaconBackend;
import com.marshmallow.beacon.backend.FirebaseBackend;
import com.marshmallow.beacon.broadcasts.ContactUpdateBroadcast;
import com.marshmallow.beacon.models.Contact;

import java.util.List;
import java.util.Vector;

/**
 * Created by George on 7/13/2018.
 */
public class ContactsActivity extends BaseActivity {

    // GUI handles
    private RecyclerView contactsRecyclerView;
    private RecyclerView.LayoutManager contactsLayoutManager;
    private ContactsAdapter contactsAdapter;
    private Vector<Contact> contacts;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contacts);
        super.onCreate(savedInstanceState);

        // GUI handle instantiation
        contacts = new Vector<>();
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
        contactsAdapter = new ContactsAdapter(this, contacts);
        contactsRecyclerView.setAdapter(contactsAdapter);

        // Initialize broadcastReceiver and its filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(ContactUpdateBroadcast.action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO handle the update
                Contact contact = new Contact();
                contact.setUsername(intent.getStringExtra(ContactUpdateBroadcast.usernameKey));
                contact.setDemandStatus(intent.getBooleanExtra(ContactUpdateBroadcast.demandStatusKey, false));
                contact.setSupplyStatus(intent.getBooleanExtra(ContactUpdateBroadcast.supplyStatusKey, false));
                if (UserManager.getInstance().getContacts().containsKey(contact.getUsername())) {
                    UserManager.getInstance().getContacts().get
                } else {
                    UserManager.getInstance().getContacts().put(contact.getUsername(), contact);
                    contacts.add(contact);
                }

                contactsAdapter.notifyDataSetChanged();
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
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
